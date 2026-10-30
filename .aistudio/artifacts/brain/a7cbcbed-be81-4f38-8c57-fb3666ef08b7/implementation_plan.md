# Quantum Clean Solutions — Enterprise Mobile Architecture & Full-Stack System

A 24/7 automated, voice-first business generation engine and 3-in-1 interactive mobile portal (Customer, Dealer, Super Admin) for Quantum Clean Solutions. The system combines an industrial & domestic cleaning mega-store, AI Voice-to-WhatsApp ordering, dynamic geo-connect dealer routing, automated multi-partner logistics (Porter/Dunzo/Rapido), and predictive dealer inventory management with zero-maintenance single-handed operations.

---

### User Review & Critical Decisions

> [!IMPORTANT]
> The architectural design combines a production-grade Supabase/PostgreSQL schema, backend automation pipelines (n8n/Make + Next.js), and an interactive Android application (Jetpack Compose) featuring all three role portals (Customer Mega Store, Dealer CRM/Marketing/Dispatch, and Founder God-View Super Admin), along with an embedded SQL & System Architecture viewer.

- **Confirmed Mode Architecture**: 3-in-1 Role Portal with real-time switching between Customer, Dealer, and Super Admin modes.
- **Confirmed Checkout & Payment Flow**: WhatsApp Business API trigger with direct 'Quantum Cash' loyalty wallet point deductions, backed by Razorpay/UPI links and Dealer Credit terms.
- **Confirmed Logistics Strategy**: Automated multi-carrier dispatch routing heavy chemical containers and bulk drums (20L, 50L, 200L, 1000L IBC) via Porter API, and domestic consumer refills (500ml, 1L, 5L) via Dunzo/Rapido on-demand couriers.
- **Confirmed Legal Engine**: Structured consent logging for version-controlled Terms & Conditions, Privacy Policy, and B2B Return/Exchange policies.

---

### 1. Overview & Core Concept

**Quantum Clean Solutions** manufactures and distributes high-performance cleaning formulations across India:
- **Domestic Range**: STAINOFF Liquid Detergent, SWIPE SHINE Dish Wash, FLUSH MAX Toilet Cleaner, DE-SCALE PRO Limescale Remover, HYGIENIX Hand Wash, AROMIST Air Freshener, BATH CARE PRO X, CRYSTAL PRO X Glass Cleaner.
- **B2B & Commercial Range**: Commercial Washroom Sanitizer (50L), Kitchen Cleaner & Degreaser (5L/20L), Heavy Duty Alkaline Degreaser (200L/1000L IBC), Floor Cleaner (Lime/Lavender).
- **Industrial & OEM Contract Manufacturing**: Custom chemical formulation, private labeling, bulk drum supply, contract packaging.

The system addresses three critical business objectives:
1. **Drive Sales & B2B Volume**: "Best Price" psychology matrix highlighting 20–40% cost savings against brand-name competitors; instant volume tier discounts; automated B2B quotation generation.
2. **Dealer Enablement & Lead Routing**: Dynamic Geo-Connect Matrix routes orders to the nearest stocking dealer. Micro-targeted Meta ad tools empower dealers to generate local inquiries that route directly to their WhatsApp.
3. **Single-Handed Super Admin**: Automated predictive restock alerts (WhatsApp), hands-off courier booking webhooks, and comprehensive revenue/inventory telemetry.

---

### 2. User Experience & Visual Design

#### Visual Identity & Theme
- **Brand Colors**: 
  - Primary: Deep Quantum Blue (`#0F4C81` / `#1E3A8A`) — conveys industrial strength, trust, and cleanliness.
  - Accent / Vibrant: Quantum Bio Green (`#10B981` / `#059669`) — signifies eco-friendly formulas, freshness, and growth.
  - Surface & Backgrounds: Crisp clean light surfaces (`#F8FAFC`) with deep sapphire-tinted dark containers (`#0B132B`).
  - Best Price Tag: Electric Lime & Amber Gold (`#F59E0B` / `#84CC16`) for instant competitor savings callouts.
- **Typography**: Clean, robust corporate typography with high legibility for product specifications, chemical dosages, and financial figures.
- **Branding Assets**: Direct integration of real product line photography from the brand catalogue (banners, canisters, refill pouches, 1000L IBC tanks, and OEM factory lines).

#### 3-in-1 Role Portals
```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       QUANTUM CLEAN SOLUTIONS PORTAL                         │
├─────────────────────┬───────────────────────────┬───────────────────────────┤
│    CUSTOMER MODE    │        DEALER MODE        │     SUPER ADMIN MODE      │
├─────────────────────┼───────────────────────────┼───────────────────────────┤
│ • Mega Store        │ • Geo-Connect Lead Radar  │ • Global Inventory Map    │
│ • Price Comparison  │ • Dynamic Radius Settings │ • AI Revenue Analytics    │
│ • Voice AI Order    │ • 3-Day Restock Warnings  │ • Active Dealer Ad Spend  │
│ • WhatsApp Checkout │ • Meta Local Ad Launcher  │ • Dispatch Webhook Hub    │
│ • Quantum Cash      │ • 1-Click Porter / Dunzo  │ • SQL Schema & Docs       │
└─────────────────────┴───────────────────────────┴───────────────────────────┘
```

1. **Customer Mega Store**:
   - Filter by Domestic, B2B, Industrial, or OEM Contract Manufacturing.
   - Live "Quantum Price vs Competitor Price" comparative badge (e.g., Quantum ₹349 vs Leading Brand ₹499 → Save ₹150 / 30%).
   - One-touch Voice Ordering: Tap mic, speak in natural English/Hindi (e.g., *"Send 4 cans of 5L Degrease Pro and 10 boxes of Flush Max to my workshop"*), AI converts speech to structured cart.
   - WhatsApp Direct Checkout with automated 'Quantum Cash' balance deduction.

2. **Dealer Portal**:
   - Incoming Lead Radar: View orders dynamically routed to the dealer's pin code.
   - Predictive Stock Forecaster: Flags items with `< 3 days` projected inventory and one-tap restock PO via WhatsApp.
   - Micro-Targeted Ad Hub: Launch geo-fenced FB/Instagram ads within 5km for ₹500/day; leads click straight into Dealer WhatsApp.
   - Automated Logistics Dispatcher: View pending dispatches with auto-assigned Porter or Dunzo pickups.

3. **Super Admin God-View**:
   - Master KPI telemetry: GMV, AI-assisted order volume, Quantum Cash circulation, active dealer radius efficiency.
   - Global inventory heat-map across warehouses and regional dealer hubs.
   - Full Supabase SQL Database schema inspector and export facility.
   - Legal consent audit log.

---

### 3. Technical Architecture & Data Strategy

```
                               ┌────────────────────────────────┐
                               │   Mobile / Next.js Frontend    │
                               │  (Customer / Dealer / Admin)   │
                               └──────────────┬─────────────────┘
                                              │
                       ┌──────────────────────┴──────────────────────┐
                       ▼                                             ▼
       ┌───────────────────────────────┐             ┌───────────────────────────────┐
       │   AI Voice & Intent Engine    │             │   Supabase (PostgreSQL 15)    │
       │   Whisper / Gemini AI Model   │             │   • Auth & Role-Based RLS     │
       └──────────────┬────────────────┘             │   • PostGIS Geo-Queries       │
                      │                              │   • Realtime Inventory Sync   │
                      ▼                              └───────────────┬───────────────┘
       ┌───────────────────────────────┐                             │
       │   n8n / Backend Automations   │◄────────────────────────────┘
       ├───────────────────────────────┤
       │ • WhatsApp Business Cloud API │ ──► Customer Notifications & Checkout
       │ • Meta Marketing Graph API    │ ──► Local Dealer Facebook/IG Campaigns
       │ • Logistics Dispatch Webhooks │ ──► Porter API (Bulk) / Dunzo API (Express)
       │ • Competitor Price Scraper    │ ──► Automated Daily Price Matrix Update
       │ • Predictive Restock Cron     │ ──► Low-Stock Dealer WhatsApp Alerts
       └───────────────────────────────┘
```

---

### 4. Supabase Database Schema (PostgreSQL DDL)

The complete SQL schema covers all 8 domain tables with relations, constraints, and audit logging:

```sql
-- ============================================================================
-- QUANTUM CLEAN SOLUTIONS - PRODUCTION SUPABASE DATABASE SCHEMA
-- ============================================================================

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "postgis";

-- 1. USERS & ROLES
CREATE TYPE user_role AS ENUM ('customer_b2c', 'buyer_b2b', 'dealer', 'super_admin');

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    phone VARCHAR(20) UNIQUE NOT NULL,
    full_name VARCHAR(120) NOT NULL,
    email VARCHAR(120) UNIQUE,
    role user_role DEFAULT 'customer_b2c',
    gstin VARCHAR(15), -- For B2B / Dealer verification
    company_name VARCHAR(150),
    is_verified BOOLEAN DEFAULT FALSE,
    wallet_balance NUMERIC(12, 2) DEFAULT 0.00 CHECK (wallet_balance >= 0),
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 2. DEALER PROFILES & GEOLOCATION
CREATE TABLE dealers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    store_name VARCHAR(150) NOT NULL,
    whatsapp_number VARCHAR(20) NOT NULL,
    address_line TEXT NOT NULL,
    pincode VARCHAR(10) NOT NULL,
    location GEOGRAPHY(Point, 4326) NOT NULL,
    base_service_radius_km NUMERIC(5, 2) DEFAULT 10.00,
    max_service_radius_km NUMERIC(5, 2) DEFAULT 50.00,
    is_active BOOLEAN DEFAULT TRUE,
    rating NUMERIC(3, 2) DEFAULT 5.00,
    daily_order_capacity INT DEFAULT 50,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 3. PRODUCT CATALOG & COMPARISON MATRIX
CREATE TYPE product_range AS ENUM ('domestic', 'b2b', 'industrial', 'oem_custom');
CREATE TYPE product_category AS ENUM ('chemicals', 'tools', 'machines', 'accessories');

CREATE TABLE products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    sku VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(150) NOT NULL,
    brand_subname VARCHAR(80), -- e.g. 'STAINOFF', 'DEGREASE PRO'
    range product_range NOT NULL,
    category product_category NOT NULL,
    packaging_size VARCHAR(50) NOT NULL, -- e.g. '500ml', '5L', '20L', '200L', '1000L'
    weight_kg NUMERIC(8, 2) NOT NULL,
    b2c_price NUMERIC(10, 2) NOT NULL,
    b2b_price NUMERIC(10, 2) NOT NULL,
    dealer_price NUMERIC(10, 2) NOT NULL,
    competitor_name VARCHAR(100) DEFAULT 'Leading Brand',
    competitor_price NUMERIC(10, 2) NOT NULL,
    cashback_quantum_cash NUMERIC(10, 2) DEFAULT 0.00,
    image_url TEXT,
    description TEXT,
    dilution_ratio VARCHAR(80),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 4. BULK TIERED PRICING MATRIX
CREATE TABLE bulk_pricing_tiers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    min_quantity INT NOT NULL,
    discount_percentage NUMERIC(5, 2) NOT NULL,
    custom_unit_price NUMERIC(10, 2)
);

-- 5. DEALER INVENTORY & DEMAND PREDICTION
CREATE TABLE dealer_inventory (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    dealer_id UUID REFERENCES dealers(id) ON DELETE CASCADE,
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    stock_quantity INT DEFAULT 0 CHECK (stock_quantity >= 0),
    reserved_quantity INT DEFAULT 0,
    avg_daily_sales NUMERIC(8, 2) DEFAULT 1.0,
    safety_stock_days INT DEFAULT 3,
    last_restocked_at TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(dealer_id, product_id)
);

-- 6. ORDERS & FULFILLMENT
CREATE TYPE order_status AS ENUM ('draft', 'placed', 'routed_to_dealer', 'confirmed', 'dispatched', 'delivered', 'cancelled');
CREATE TYPE order_channel AS ENUM ('app_ui', 'ai_voice', 'whatsapp_bot', 'b2b_quotation');
CREATE TYPE dispatch_carrier AS ENUM ('dealer_self', 'porter', 'dunzo', 'rapido');

CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_number VARCHAR(30) UNIQUE NOT NULL,
    customer_id UUID REFERENCES users(id),
    dealer_id UUID REFERENCES dealers(id),
    total_mrp NUMERIC(12, 2) NOT NULL,
    quantum_discount NUMERIC(12, 2) DEFAULT 0.00,
    quantum_cash_applied NUMERIC(12, 2) DEFAULT 0.00,
    final_payable NUMERIC(12, 2) NOT NULL,
    earned_quantum_cash NUMERIC(12, 2) DEFAULT 0.00,
    delivery_address TEXT NOT NULL,
    delivery_pincode VARCHAR(10) NOT NULL,
    delivery_location GEOGRAPHY(Point, 4326),
    channel order_channel DEFAULT 'app_ui',
    status order_status DEFAULT 'placed',
    assigned_carrier dispatch_carrier,
    carrier_tracking_id VARCHAR(100),
    search_radius_used_km NUMERIC(5, 2) DEFAULT 10.00,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    order_id UUID REFERENCES orders(id) ON DELETE CASCADE,
    product_id UUID REFERENCES products(id),
    quantity INT NOT NULL CHECK (quantity > 0),
    unit_price NUMERIC(10, 2) NOT NULL,
    total_price NUMERIC(12, 2) NOT NULL
);

-- 7. QUANTUM CASH WALLET TRANSACTIONS
CREATE TYPE wallet_tx_type AS ENUM ('cashback_credit', 'order_discount_debit', 'b2b_contract_bonus', 'admin_adjustment');

CREATE TABLE wallet_transactions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    order_id UUID REFERENCES orders(id),
    amount NUMERIC(10, 2) NOT NULL,
    tx_type wallet_tx_type NOT NULL,
    notes TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 8. DEALER MICRO-ADS CAMPAIGNS
CREATE TYPE ad_platform AS ENUM ('facebook_instagram', 'whatsapp_status', 'google_local');
CREATE TYPE ad_status AS ENUM ('draft', 'pending_approval', 'running', 'completed', 'paused');

CREATE TABLE dealer_ads (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    dealer_id UUID REFERENCES dealers(id) ON DELETE CASCADE,
    product_id UUID REFERENCES products(id),
    campaign_name VARCHAR(150) NOT NULL,
    daily_budget_inr NUMERIC(10, 2) NOT NULL,
    target_radius_km NUMERIC(5, 2) DEFAULT 5.0,
    meta_campaign_id VARCHAR(100),
    status ad_status DEFAULT 'pending_approval',
    leads_generated INT DEFAULT 0,
    clicks INT DEFAULT 0,
    started_at TIMESTAMPTZ,
    expires_at TIMESTAMPTZ
);

-- 9. LEGAL & COMPLIANCE CONSENT LOGS
CREATE TYPE policy_type AS ENUM ('terms_and_conditions', 'privacy_policy', 'b2b_supply_agreement', 'return_refund_policy');

CREATE TABLE legal_consents (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE CASCADE,
    policy_type policy_type NOT NULL,
    version VARCHAR(20) NOT NULL,
    ip_address VARCHAR(50),
    user_agent TEXT,
    consented_at TIMESTAMPTZ DEFAULT NOW()
);

-- 10. COMPETITOR PRICE SCRAPING MATRIX
CREATE TABLE competitor_price_matrix (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    product_id UUID REFERENCES products(id) ON DELETE CASCADE,
    competitor_brand VARCHAR(100) NOT NULL,
    competitor_product_name VARCHAR(150) NOT NULL,
    competitor_price NUMERIC(10, 2) NOT NULL,
    source_url TEXT,
    last_scraped_at TIMESTAMPTZ DEFAULT NOW()
);
```

---

### 5. Step-by-Step Logic & Integrations

#### A. Dynamic Geo-Connect Matrix (Lead Routing Algorithm)
```python
def route_order_to_nearest_dealer(order_id, user_lat, user_lon, order_items):
    initial_radius_km = 10.0
    expansion_step_km = 10.0
    max_radius_km = 50.0
    current_radius = initial_radius_km

    while current_radius <= max_radius_km:
        # Spatial query using PostGIS: Find active dealers within current_radius
        candidate_dealers = db.query("""
            SELECT d.id, d.whatsapp_number,
                   ST_Distance(d.location, ST_SetSRID(ST_Point(:lon, :lat), 4326)::geography) / 1000.0 AS dist_km
            FROM dealers d
            WHERE d.is_active = TRUE
              AND ST_DWithin(d.location, ST_SetSRID(ST_Point(:lon, :lat), 4326)::geography, :radius_meters)
            ORDER BY dist_km ASC
        """, {"lat": user_lat, "lon": user_lon, "radius_meters": current_radius * 1000})

        for dealer in candidate_dealers:
            # Check inventory sufficiency for all items in order
            has_sufficient_stock = True
            for item in order_items:
                stock = db.query("""
                    SELECT (stock_quantity - reserved_quantity) AS available
                    FROM dealer_inventory
                    WHERE dealer_id = :dealer_id AND product_id = :product_id
                """, {"dealer_id": dealer.id, "product_id": item.product_id}).scalar()
                
                if not stock or stock < item.quantity:
                    has_sufficient_stock = False
                    break
            
            if has_sufficient_stock:
                # Reserve stock & assign order
                assign_order_to_dealer(order_id, dealer.id, current_radius)
                notify_dealer_whatsapp(dealer.whatsapp_number, order_id)
                return {"status": "routed", "dealer_id": dealer.id, "radius_km": current_radius}

        # Expand radius if no dealer in current circle has sufficient inventory
        current_radius += expansion_step_km

    # Fallback to Central Quantum Factory Warehouse
    assign_order_to_central_factory(order_id)
    return {"status": "routed_factory", "reason": "no_dealer_in_50km"}
```

#### B. Voice-to-WhatsApp Checkout Workflow
```
[User speaks audio in App]
           │
           ▼
[Whisper / Speech-to-Text] ──► Extracted Transcript: "I need 2 cans of 5L Stainoff and 1 box of Dishwash"
           │
           ▼
[LLM Intent Matching] ──► Structured Output:
                         {
                           "intent": "create_order",
                           "items": [
                             {"sku": "Q-SO-5L", "qty": 2},
                             {"sku": "Q-SS-1L", "qty": 1}
                           ]
                         }
           │
           ▼
[Cart Calculation] ──► Subtotal: ₹1,550
                      Quantum Cash Available: ₹200 (Auto-deducted)
                      Payable: ₹1,350 (Save ₹450 vs Brand X)
           │
           ▼
[WhatsApp Business API] ──► Sends Rich Interactive Order Confirmation:
                           "Hi Rajesh! Your Quantum Clean order Q-8492 is ready.
                            Total: ₹1,350 (₹200 Quantum Cash deducted).
                            Tap below to pay via UPI or confirm Cash on Delivery."
```

#### C. Multi-Partner Logistics Dispatch Matrix
- **Weight Threshold**: If total order weight $\ge 30\text{ kg}$ (e.g., 20L canisters, 50L drums, 200L barrels, or 10+ crates):
  - **Auto-Trigger**: Porter API webhook with vehicle class (`3-Wheeler / 407 Truck`).
- **Express Domestic Refills**: If weight $< 30\text{ kg}$ and delivery distance $\le 15\text{ km}$:
  - **Auto-Trigger**: Dunzo / Rapido courier pickup directly from dealer store.
- **Fallback**: Dealer self-dispatch with in-app OTP confirmation.

#### D. Predictive Restock Alerting Engine
- Scheduled daily cron calculates:
  $$\text{Days of Cover} = \frac{\text{Available Stock}}{\text{Average Daily Velocity (last 14 days)}}$$
- If $\text{Days of Cover} \le 3$ days:
  - System generates restock warning.
  - Sends automated WhatsApp message to dealer with 1-click reorder PO at distributor tier pricing.

#### E. Ethical Competitor Price Scraping
- Daily lightweight scraper targeting top cleaning brands and e-commerce listings.
- Strictly adheres to `robots.txt` and domain rate limits ($1\text{ req}/5\text{s}$).
- Automatically updates `competitor_price_matrix`, ensuring Quantum's 20–40% lower cost advantage is proven live in the app.

---

### 6. Mobile Implementation Plan (Android / Jetpack Compose)

1. **Brand System & Assets**:
   - Integrate Quantum logo and visual assets (`quantum_cleaning_products_banner_03e4e2d7.jpg`, product canisters, and OEM photography).
   - Dynamic M3 color palette centered on Quantum Blue & Bio Green.

2. **Role Switcher Navigation**:
   - Header role selector allowing instant switching between **Customer**, **Dealer**, and **Super Admin**.

3. **Customer Portal Screens**:
   - Mega Store Catalog: Segmented by Domestic, B2B, Industrial, Tools & Machines.
   - Price Advantage Matrix: Visual cards displaying Quantum Price vs Competitor Price with percentage saved.
   - AI Voice Command Bar: Interactive speech simulation and voice cart builder.
   - WhatsApp Checkout Dialog with Quantum Cash wallet deduction slider.

4. **Dealer Portal Screens**:
   - Geo-Connect Lead Radar: Live visualizer showing order incoming location, distance, and inventory match.
   - Predictive Stock Monitor: Inventory table with 3-day projection warnings and 1-click WhatsApp restock buttons.
   - Micro-Targeted Ad Hub: Template selector (e.g., "Degreaser Promo"), budget slider (₹300 - ₹2,000), and lead generation forecast.
   - Courier Dispatcher: 1-click Porter / Dunzo integration.

5. **Super Admin Screens**:
   - God-view dashboard with business KPIs: Total GMV, AI-driven orders, Dealer network radius health.
   - Complete Interactive Supabase SQL Table Viewer & Copy tool.
   - Next.js & n8n architecture pipeline viewer.

6. **Legal & Compliance**:
   - Modal bottom sheet displaying Terms of Service, Privacy Policy, and B2B Exchange Policy with timestamped consent logging.

---

### Verification & Testing
- Compile and test the application with `compile_applet`.
- Verify role switching, cart additions, price comparisons, voice intent simulations, and dealer restock alerts.
