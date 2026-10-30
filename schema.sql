-- ============================================================================
-- QUANTUM CLEAN SOLUTIONS - COMPLETE MODULAR SUPABASE/POSTGRESQL SCHEMA
-- ============================================================================
-- Modules covered:
-- 1. Users, Roles & Profiles
-- 2. Products, Categories & Pricing Matrix
-- 3. Dealers & Geolocation (PostGIS)
-- 4. Inventory, Safety Stock & Predictive Restock Alerts
-- 5. Orders, Line Items & Status Lifecycle
-- 6. Wallet Transactions & Quantum Cash Rewards (Strict Factory Credit)
-- 7. Logistics Tracking & Multi-Carrier Automated Dispatch (Porter/Dunzo/Rapido)
-- 8. Legal Consents, Terms & Regulatory Audit Trail
-- 9. Competitor Price Comparison Scraping Matrix
-- 10. Dealer Micro-Targeted Meta Marketing Campaigns
-- ============================================================================

-- Core Extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "postgis";

-- ----------------------------------------------------------------------------
-- MODULE 1: ROLES, USERS & AUTHENTICATION
-- ----------------------------------------------------------------------------
CREATE TYPE user_role AS ENUM (
    'customer_b2c',      -- Retail / Domestic buyer
    'buyer_b2b',         -- Commercial / Hospitality / Hospital buyer
    'dealer',            -- Authorized regional distributor / station
    'super_admin'        -- Founder / God-view operator
);

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    supabase_auth_id UUID UNIQUE, -- Mapped to auth.users in Supabase
    phone VARCHAR(20) UNIQUE NOT NULL,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(120) UNIQUE,
    role user_role DEFAULT 'customer_b2c' NOT NULL,
    gstin VARCHAR(15), -- Required for verified B2B buyers and dealers
    company_name VARCHAR(150),
    is_verified BOOLEAN DEFAULT FALSE,
    wallet_balance NUMERIC(12, 2) DEFAULT 0.00 CHECK (wallet_balance >= 0),
    created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_users_phone ON users(phone);
CREATE INDEX idx_users_role ON users(role);

-- ----------------------------------------------------------------------------
-- MODULE 2: DEALERS & DYNAMIC GEOLOCATION MATRIX
-- ----------------------------------------------------------------------------
CREATE TABLE dealers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    store_name VARCHAR(150) NOT NULL,
    whatsapp_number VARCHAR(20) NOT NULL,
    address_line TEXT NOT NULL,
    city VARCHAR(80) NOT NULL,
    state VARCHAR(80) NOT NULL,
    pincode VARCHAR(10) NOT NULL,
    location GEOGRAPHY(Point, 4326) NOT NULL, -- Latitude / Longitude coordinates
    base_service_radius_km NUMERIC(5, 2) DEFAULT 10.00 CHECK (base_service_radius_km > 0),
    max_service_radius_km NUMERIC(5, 2) DEFAULT 50.00 CHECK (max_service_radius_km >= base_service_radius_km),
    is_active BOOLEAN DEFAULT TRUE NOT NULL,
    rating NUMERIC(3, 2) DEFAULT 5.00 CHECK (rating >= 1.0 AND rating <= 5.0),
    daily_order_capacity INT DEFAULT 50 CHECK (daily_order_capacity > 0),
    created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_dealers_location ON dealers USING GIST(location);
CREATE INDEX idx_dealers_pincode ON dealers(pincode);
CREATE INDEX idx_dealers_is_active ON dealers(is_active);

-- ----------------------------------------------------------------------------
-- MODULE 3: PRODUCTS, DYNAMIC PRICING & BEST PRICE MATRIX
-- ----------------------------------------------------------------------------
CREATE TYPE product_range AS ENUM (
    'domestic',
    'b2b',
    'industrial',
    'oem_contract'
);

CREATE TYPE product_category AS ENUM (
    'chemicals',
    'tools',
    'machines',
    'accessories'
);

CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sku VARCHAR(60) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL,
    brand_subname VARCHAR(100), -- e.g., 'STAINOFF', 'SWIPE SHINE', 'DE-SCALE PRO'
    range product_range NOT NULL,
    category product_category NOT NULL,
    packaging_size VARCHAR(50) NOT NULL, -- e.g., '500ml', '1L', '5L', '20L', '200L', '1000L'
    weight_kg NUMERIC(8, 2) NOT NULL CHECK (weight_kg > 0),
    b2c_price NUMERIC(10, 2) NOT NULL CHECK (b2c_price >= 0),
    b2b_price NUMERIC(10, 2) NOT NULL CHECK (b2b_price >= 0),
    dealer_price NUMERIC(10, 2) NOT NULL CHECK (dealer_price >= 0),
    competitor_brand VARCHAR(100) DEFAULT 'Leading Brand',
    competitor_price NUMERIC(10, 2) NOT NULL CHECK (competitor_price >= 0),
    cashback_quantum_cash NUMERIC(10, 2) DEFAULT 0.00 CHECK (cashback_quantum_cash >= 0),
    image_url TEXT,
    description TEXT,
    dilution_ratio VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_products_range ON products(range);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_sku ON products(sku);

-- Bulk Volume Tiered Pricing
CREATE TABLE bulk_pricing_tiers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID REFERENCES products(id) ON DELETE CASCADE NOT NULL,
    min_quantity INT NOT NULL CHECK (min_quantity > 0),
    discount_percentage NUMERIC(5, 2) NOT NULL CHECK (discount_percentage >= 0 AND discount_percentage <= 100),
    custom_unit_price NUMERIC(10, 2) CHECK (custom_unit_price >= 0),
    created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_bulk_tiers_product ON bulk_pricing_tiers(product_id);

-- ----------------------------------------------------------------------------
-- MODULE 4: DEALER INVENTORY & PREDICTIVE RESTOCK MONITORING
-- ----------------------------------------------------------------------------
CREATE TABLE dealer_inventory (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    dealer_id UUID REFERENCES dealers(id) ON DELETE CASCADE NOT NULL,
    product_id UUID REFERENCES products(id) ON DELETE CASCADE NOT NULL,
    stock_quantity INT DEFAULT 0 NOT NULL CHECK (stock_quantity >= 0),
    reserved_quantity INT DEFAULT 0 NOT NULL CHECK (reserved_quantity >= 0 AND reserved_quantity <= stock_quantity),
    avg_daily_sales NUMERIC(8, 2) DEFAULT 1.0 NOT NULL CHECK (avg_daily_sales >= 0),
    safety_stock_days INT DEFAULT 3 NOT NULL CHECK (safety_stock_days >= 1),
    last_restocked_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    UNIQUE(dealer_id, product_id)
);

CREATE INDEX idx_inventory_dealer ON dealer_inventory(dealer_id);
CREATE INDEX idx_inventory_product ON dealer_inventory(product_id);

-- Helper View for Days of Cover & Restock Status
CREATE OR REPLACE VIEW view_dealer_restock_alerts AS
SELECT 
    di.id AS inventory_id,
    d.id AS dealer_id,
    d.store_name,
    d.whatsapp_number,
    p.id AS product_id,
    p.name AS product_name,
    p.packaging_size,
    di.stock_quantity,
    di.reserved_quantity,
    (di.stock_quantity - di.reserved_quantity) AS available_quantity,
    di.avg_daily_sales,
    di.safety_stock_days,
    CASE 
        WHEN di.avg_daily_sales > 0 THEN 
            ROUND(((di.stock_quantity - di.reserved_quantity) / di.avg_daily_sales)::numeric, 1)
        ELSE 999.0 
    END AS days_of_cover,
    CASE 
        WHEN di.avg_daily_sales > 0 AND ((di.stock_quantity - di.reserved_quantity) / di.avg_daily_sales) <= di.safety_stock_days 
        THEN TRUE 
        ELSE FALSE 
    END AS needs_restock_alert,
    GREATEST(ROUND((di.avg_daily_sales * 14) - (di.stock_quantity - di.reserved_quantity)), 20) AS suggested_restock_units
FROM dealer_inventory di
JOIN dealers d ON di.dealer_id = d.id
JOIN products p ON di.product_id = p.id;

-- ----------------------------------------------------------------------------
-- MODULE 5: ORDERS, LINE ITEMS & DYNAMIC FULFILLMENT
-- ----------------------------------------------------------------------------
CREATE TYPE order_status AS ENUM (
    'draft',
    'placed',
    'routed_to_dealer',
    'confirmed',
    'dispatched',
    'delivered',
    'cancelled'
);

CREATE TYPE order_channel AS ENUM (
    'app_ui',
    'ai_voice',
    'whatsapp_bot',
    'b2b_quotation'
);

CREATE TYPE dispatch_carrier AS ENUM (
    'dealer_self',
    'porter',
    'dunzo',
    'rapido',
    'factory_freight'
);

CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_number VARCHAR(30) UNIQUE NOT NULL,
    customer_id UUID REFERENCES users(id) NOT NULL,
    dealer_id UUID REFERENCES dealers(id), -- Nullable if routed to factory
    total_mrp NUMERIC(12, 2) NOT NULL CHECK (total_mrp >= 0),
    quantum_discount NUMERIC(12, 2) DEFAULT 0.00 CHECK (quantum_discount >= 0),
    quantum_cash_applied NUMERIC(12, 2) DEFAULT 0.00 CHECK (quantum_cash_applied >= 0),
    final_payable NUMERIC(12, 2) NOT NULL CHECK (final_payable >= 0),
    earned_quantum_cash NUMERIC(12, 2) DEFAULT 0.00 CHECK (earned_quantum_cash >= 0),
    delivery_address TEXT NOT NULL,
    delivery_city VARCHAR(80) NOT NULL,
    delivery_pincode VARCHAR(10) NOT NULL,
    delivery_location GEOGRAPHY(Point, 4326),
    channel order_channel DEFAULT 'app_ui' NOT NULL,
    status order_status DEFAULT 'placed' NOT NULL,
    assigned_carrier dispatch_carrier DEFAULT 'dealer_self',
    search_radius_used_km NUMERIC(5, 2) DEFAULT 10.00,
    created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_orders_customer ON orders(customer_id);
CREATE INDEX idx_orders_dealer ON orders(dealer_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);

CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID REFERENCES orders(id) ON DELETE CASCADE NOT NULL,
    product_id UUID REFERENCES products(id) NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(10, 2) NOT NULL CHECK (unit_price >= 0),
    total_price NUMERIC(12, 2) NOT NULL CHECK (total_price >= 0),
    created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_product ON order_items(product_id);

-- ----------------------------------------------------------------------------
-- MODULE 6: QUANTUM CASH WALLET TRANSACTIONS
-- ----------------------------------------------------------------------------
CREATE TYPE wallet_tx_type AS ENUM (
    'cashback_credit',          -- Auto cashback from completed purchases
    'order_discount_debit',     -- Applied as direct deduction at checkout
    'b2b_contract_bonus',       -- Institutional volume incentive
    'admin_adjustment'          -- Reversal or manual dispute resolution
);

CREATE TABLE wallet_transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    order_id UUID REFERENCES orders(id) ON DELETE SET NULL,
    amount NUMERIC(10, 2) NOT NULL CHECK (amount <> 0),
    tx_type wallet_tx_type NOT NULL,
    running_balance NUMERIC(12, 2) NOT NULL CHECK (running_balance >= 0),
    notes TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_wallet_tx_user ON wallet_transactions(user_id);
CREATE INDEX idx_wallet_tx_order ON wallet_transactions(order_id);

-- ----------------------------------------------------------------------------
-- MODULE 7: LOGISTICS TRACKING & MULTI-CARRIER WEBHOOKS
-- ----------------------------------------------------------------------------
CREATE TABLE logistics_tracking (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID REFERENCES orders(id) ON DELETE CASCADE NOT NULL,
    carrier dispatch_carrier NOT NULL,
    carrier_order_id VARCHAR(100), -- Porter Order ID, Dunzo Task ID, etc.
    tracking_url TEXT,
    vehicle_type VARCHAR(60),      -- e.g. '3-Wheeler Tata Ace', 'Two-Wheeler Partner', 'Tata 407'
    driver_name VARCHAR(100),
    driver_phone VARCHAR(20),
    pickup_pincode VARCHAR(10) NOT NULL,
    delivery_pincode VARCHAR(10) NOT NULL,
    total_weight_kg NUMERIC(8, 2) NOT NULL CHECK (total_weight_kg > 0),
    dispatch_status VARCHAR(60) DEFAULT 'order_created' NOT NULL,
    webhook_payload JSONB,
    dispatched_at TIMESTAMPTZ,
    delivered_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_logistics_order ON logistics_tracking(order_id);
CREATE INDEX idx_logistics_carrier ON logistics_tracking(carrier);
CREATE INDEX idx_logistics_status ON logistics_tracking(dispatch_status);

-- ----------------------------------------------------------------------------
-- MODULE 8: LEGAL CONSENTS & REGULATORY AUDIT LOG
-- ----------------------------------------------------------------------------
CREATE TYPE legal_policy_type AS ENUM (
    'terms_and_conditions',
    'privacy_policy',
    'b2b_supply_agreement',
    'return_refund_policy'
);

CREATE TABLE legal_consents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    order_id UUID REFERENCES orders(id) ON DELETE SET NULL, -- Logged if consented during checkout
    policy_type legal_policy_type NOT NULL,
    version VARCHAR(30) NOT NULL, -- e.g., 'v2.4_2026'
    ip_address VARCHAR(50),
    user_agent TEXT,
    consent_channel VARCHAR(40) DEFAULT 'app_ui', -- 'app_ui', 'whatsapp_bot', 'b2b_portal'
    consented_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_legal_consents_user ON legal_consents(user_id);
CREATE INDEX idx_legal_consents_policy ON legal_consents(policy_type);
CREATE INDEX idx_legal_consents_consented_at ON legal_consents(consented_at);

-- ----------------------------------------------------------------------------
-- MODULE 9: COMPETITOR PRICE SCRAPING MATRIX
-- ----------------------------------------------------------------------------
CREATE TABLE competitor_price_matrix (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID REFERENCES products(id) ON DELETE CASCADE NOT NULL,
    competitor_brand VARCHAR(100) NOT NULL,
    competitor_product_name VARCHAR(150) NOT NULL,
    competitor_price NUMERIC(10, 2) NOT NULL CHECK (competitor_price >= 0),
    source_url TEXT,
    is_active_benchmark BOOLEAN DEFAULT TRUE NOT NULL,
    last_scraped_at TIMESTAMPTZ DEFAULT NOW() NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_competitor_matrix_product ON competitor_price_matrix(product_id);
CREATE INDEX idx_competitor_matrix_scraped_at ON competitor_price_matrix(last_scraped_at);

-- ----------------------------------------------------------------------------
-- MODULE 10: DEALER MICRO-TARGETED META MARKETING CAMPAIGNS
-- ----------------------------------------------------------------------------
CREATE TYPE campaign_status AS ENUM (
    'draft',
    'pending_meta_approval',
    'active',
    'paused',
    'completed'
);

CREATE TABLE dealer_ads (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    dealer_id UUID REFERENCES dealers(id) ON DELETE CASCADE NOT NULL,
    product_id UUID REFERENCES products(id) ON DELETE CASCADE NOT NULL,
    campaign_name VARCHAR(160) NOT NULL,
    daily_budget_inr NUMERIC(10, 2) NOT NULL CHECK (daily_budget_inr >= 100.0),
    target_radius_km NUMERIC(5, 2) DEFAULT 5.00 CHECK (target_radius_km > 0),
    meta_campaign_id VARCHAR(100),
    status campaign_status DEFAULT 'pending_meta_approval' NOT NULL,
    leads_whatsapp_pings INT DEFAULT 0 CHECK (leads_whatsapp_pings >= 0),
    impressions INT DEFAULT 0 CHECK (impressions >= 0),
    clicks INT DEFAULT 0 CHECK (clicks >= 0),
    started_at TIMESTAMPTZ,
    expires_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ DEFAULT NOW() NOT NULL
);

CREATE INDEX idx_dealer_ads_dealer ON dealer_ads(dealer_id);
CREATE INDEX idx_dealer_ads_status ON dealer_ads(status);

-- ----------------------------------------------------------------------------
-- STORED FUNCTION: DYNAMIC RADIUS GEO-CONNECT LEAD ROUTING
-- ----------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION find_and_route_order_dealer(
    p_order_id UUID,
    p_delivery_lat DOUBLE PRECISION,
    p_delivery_lon DOUBLE PRECISION,
    p_initial_radius_km NUMERIC DEFAULT 10.0,
    p_expansion_step_km NUMERIC DEFAULT 10.0,
    p_max_radius_km NUMERIC DEFAULT 50.0
)
RETURNS JSONB
LANGUAGE plpgsql
AS $$
DECLARE
    v_current_radius NUMERIC := p_initial_radius_km;
    v_matched_dealer_id UUID := NULL;
    v_matched_dist_km NUMERIC := NULL;
    v_order_has_items BOOLEAN := FALSE;
    v_all_items_stocked BOOLEAN := FALSE;
    v_candidate RECORD;
BEGIN
    -- Check if order exists and has items
    SELECT EXISTS (SELECT 1 FROM order_items WHERE order_id = p_order_id) INTO v_order_has_items;
    IF NOT v_order_has_items THEN
        RETURN jsonb_build_object('success', false, 'reason', 'Order contains no line items');
    END IF;

    -- Iteratively expand search radius until a stocking dealer is found
    WHILE v_current_radius <= p_max_radius_km LOOP
        FOR v_candidate IN
            SELECT 
                d.id AS dealer_id,
                d.whatsapp_number,
                (ST_Distance(d.location, ST_SetSRID(ST_Point(p_delivery_lon, p_delivery_lat), 4326)::geography) / 1000.0) AS dist_km
            FROM dealers d
            WHERE d.is_active = TRUE
              AND ST_DWithin(d.location, ST_SetSRID(ST_Point(p_delivery_lon, p_delivery_lat), 4326)::geography, v_current_radius * 1000.0)
            ORDER BY dist_km ASC
        LOOP
            -- Check if this dealer has sufficient available inventory for ALL items in order
            v_all_items_stocked := TRUE;
            
            IF EXISTS (
                SELECT 1
                FROM order_items oi
                LEFT JOIN dealer_inventory di 
                    ON di.dealer_id = v_candidate.dealer_id 
                   AND di.product_id = oi.product_id
                WHERE oi.order_id = p_order_id
                  AND (di.stock_quantity - di.reserved_quantity) < oi.quantity
            ) THEN
                v_all_items_stocked := FALSE;
            END IF;

            IF v_all_items_stocked THEN
                v_matched_dealer_id := v_candidate.dealer_id;
                v_matched_dist_km := v_candidate.dist_km;
                EXIT; -- Matched closest stocking dealer!
            END IF;
        END LOOP;

        IF v_matched_dealer_id IS NOT NULL THEN
            EXIT; -- Found dealer
        END IF;

        v_current_radius := v_current_radius + p_expansion_step_km;
    END LOOP;

    -- If found, update order with dealer & radius
    IF v_matched_dealer_id IS NOT NULL THEN
        UPDATE orders
        SET dealer_id = v_matched_dealer_id,
            status = 'routed_to_dealer',
            search_radius_used_km = v_current_radius,
            updated_at = NOW()
        WHERE id = p_order_id;

        -- Reserve dealer inventory
        UPDATE dealer_inventory di
        SET reserved_quantity = reserved_quantity + oi.quantity,
            updated_at = NOW()
        FROM order_items oi
        WHERE di.dealer_id = v_matched_dealer_id
          AND di.product_id = oi.product_id
          AND oi.order_id = p_order_id;

        RETURN jsonb_build_object(
            'success', true,
            'routed_to', 'dealer',
            'dealer_id', v_matched_dealer_id,
            'distance_km', ROUND(v_matched_dist_km, 2),
            'radius_used_km', v_current_radius
        );
    ELSE
        -- Fallback to central factory warehouse
        UPDATE orders
        SET dealer_id = NULL,
            status = 'placed',
            search_radius_used_km = p_max_radius_km,
            assigned_carrier = 'factory_freight',
            updated_at = NOW()
        WHERE id = p_order_id;

        RETURN jsonb_build_object(
            'success', true,
            'routed_to', 'central_factory',
            'reason', 'No stocking dealer found within max radius',
            'max_radius_km', p_max_radius_km
        );
    END IF;
END;
$$;
