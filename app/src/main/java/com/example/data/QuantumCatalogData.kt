package com.example.data

import com.example.R
import com.example.model.*

object QuantumCatalogData {

    val products: List<Product> = listOf(
        Product(
            id = "p1",
            sku = "Q-STAINOFF-LAV-5L",
            name = "STAINOFF Liquid Detergent",
            subName = "3X Stain Lift Power - Lavender Fresh",
            category = ProductCategory.DOMESTIC,
            size = "5L Canister",
            weightKg = 5.2,
            b2cPrice = 499.0,
            b2bPrice = 410.0,
            dealerPrice = 340.0,
            competitorBrand = "Ariel / Surf Excel Matic 5L",
            competitorPrice = 850.0,
            cashBackQuantumCash = 50.0,
            drawableRes = R.drawable.prod_stainoff_lavender,
            description = "Tough on stains, gentle on fabrics. Active enzyme formula keeps whites whiter and colors brighter.",
            dilutionRatio = "60ml per regular 6kg wash load",
            features = listOf("3X Stain Lift Power", "Protects Fabric Strength", "Low Foam Easy Rinse", "Long Lasting Lavender Fragrance")
        ),
        Product(
            id = "p2",
            sku = "Q-STAINOFF-ENZ-2L",
            name = "STAINOFF Active Enzyme Detergent",
            subName = "Deep Stain Lift - Refill Pouch",
            category = ProductCategory.DOMESTIC,
            size = "2L Refill Pack",
            weightKg = 2.1,
            b2cPrice = 249.0,
            b2bPrice = 195.0,
            dealerPrice = 160.0,
            competitorBrand = "Tide / Surf Excel 2L",
            competitorPrice = 410.0,
            cashBackQuantumCash = 25.0,
            drawableRes = R.drawable.prod_stainoff_enzyme,
            description = "Deep clean formulation with bio-enzymes that lift stubborn dirt even in cold water.",
            dilutionRatio = "45ml per wash",
            features = listOf("Bio-Enzyme Active", "Eco-Friendly Refill", "Color Lock Technology", "Hand & Machine Wash Safe")
        ),
        Product(
            id = "p3",
            sku = "Q-SWIPESHINE-5L",
            name = "SWIPE SHINE Dish Wash Liquid",
            subName = "Lemon Fresh - Cuts Tough Grease Instantly",
            category = ProductCategory.DOMESTIC,
            size = "5L Bulk Pack",
            weightKg = 5.2,
            b2cPrice = 380.0,
            b2bPrice = 310.0,
            dealerPrice = 250.0,
            competitorBrand = "Vim / Pril 5L Commercial",
            competitorPrice = 640.0,
            cashBackQuantumCash = 40.0,
            drawableRes = R.drawable.prod_swipeshine,
            description = "Concentrated lemon-charged formula that cuts grease instantly while remaining gentle on hands.",
            dilutionRatio = "1 teaspoon (5ml) in a bowl of 40ml water",
            features = listOf("Cuts Tough Grease Instantly", "Gentle on Hands with Aloe", "Leaves Dishes Sparkling", "Non-Sticky Quick Wash")
        ),
        Product(
            id = "p4",
            sku = "Q-FLUSHMAX-5L",
            name = "FLUSH MAX Toilet Bowl Cleaner",
            subName = "Advanced Power Gel - 99.9% Germ Kill",
            category = ProductCategory.DOMESTIC,
            size = "5L Professional Can",
            weightKg = 5.3,
            b2cPrice = 399.0,
            b2bPrice = 320.0,
            dealerPrice = 265.0,
            competitorBrand = "Harpic Power Plus 5L",
            competitorPrice = 720.0,
            cashBackQuantumCash = 40.0,
            drawableRes = R.drawable.prod_flushmax,
            description = "Thick blue power gel formula that clings to toilet surfaces to eliminate limescale and yellow stains.",
            dilutionRatio = "Direct undiluted application",
            features = listOf("Kills 99.9% Germs & Bacteria", "Eliminates Hard Water Marks", "Thick Clinging Gel", "Refreshing Fragrance")
        ),
        Product(
            id = "p5",
            sku = "Q-DESCALEL-5L",
            name = "DE-SCALE PRO+ Limescale Remover",
            subName = "Dissolves Limescale & Hard Water Deposits",
            category = ProductCategory.DOMESTIC,
            size = "5L Professional Jug",
            weightKg = 5.4,
            b2cPrice = 475.0,
            b2bPrice = 390.0,
            dealerPrice = 320.0,
            competitorBrand = "Taski / Cif Descaler 5L",
            competitorPrice = 890.0,
            cashBackQuantumCash = 50.0,
            drawableRes = R.drawable.prod_descalepro,
            description = "Restores original chrome shine to taps, showers, glass partitions, and ceramic bathroom fixtures.",
            dilutionRatio = "1:2 for heavy scale, 1:5 for maintenance spray",
            features = listOf("Fast-Acting Acid Surfactant", "Non-Corrosive to Taps & Fittings", "Restores Mirror Finish", "Sparkling Bathroom Every Day")
        ),
        Product(
            id = "p6",
            sku = "Q-HYGIENIX-5L",
            name = "HYGIENIX Hand Wash",
            subName = "Sandal & Oud - pH Balanced Anti-Bacterial",
            category = ProductCategory.DOMESTIC,
            size = "5L Refill Jar",
            weightKg = 5.1,
            b2cPrice = 420.0,
            b2bPrice = 340.0,
            dealerPrice = 280.0,
            competitorBrand = "Dettol / Lifebuoy 5L",
            competitorPrice = 750.0,
            cashBackQuantumCash = 40.0,
            drawableRes = R.drawable.prod_hygienix,
            description = "Gentle moisturising liquid hand wash enriched with natural extracts and calming sandalwood aroma.",
            dilutionRatio = "Ready to use in dispenser",
            features = listOf("pH Balanced Gentle Care", "Kills 99.9% Bacteria", "Natural Moisturising Glycerin", "Exotic Sandal & Oud Fragrance")
        ),
        Product(
            id = "p7",
            sku = "Q-AROMIST-5L",
            name = "AROMIST Air Freshener",
            subName = "Lavender Bliss / Citrus Fresh - Long Lasting",
            category = ProductCategory.DOMESTIC,
            size = "5L Bulk Container",
            weightKg = 5.0,
            b2cPrice = 450.0,
            b2bPrice = 370.0,
            dealerPrice = 300.0,
            competitorBrand = "Godrej Aer / Odonil 5L",
            competitorPrice = 790.0,
            cashBackQuantumCash = 45.0,
            drawableRes = R.drawable.prod_aromist,
            description = "Neutralises airborne odours instantly and maintains invigorating fresh ambience for hours.",
            dilutionRatio = "Ready to spray",
            features = listOf("Neutralises Odours at Source", "Safe for Home, Office & Hotel", "Zero Aerosol Gas", "Calming Lavender & Citrus Note")
        ),
        Product(
            id = "p8",
            sku = "Q-CRYSTAL-5L",
            name = "CRYSTAL PRO X Glass Cleaner",
            subName = "Streak-Free Crystal Shine - Quick Dry",
            category = ProductCategory.DOMESTIC,
            size = "5L Bulk Can",
            weightKg = 5.0,
            b2cPrice = 340.0,
            b2bPrice = 270.0,
            dealerPrice = 220.0,
            competitorBrand = "Colin 5L Commercial",
            competitorPrice = 580.0,
            cashBackQuantumCash = 35.0,
            drawableRes = R.drawable.prod_crystal_prox,
            description = "Superior streak-free glass and mirror cleaner that dissolves fingerprints, dust, and haze.",
            dilutionRatio = "Ready to spray and wipe with microfiber",
            features = listOf("Streak-Free Crystal Shine", "Quick Drying Formula", "Repels Dust & Smudges", "Safe on Tinted Glass & Windows")
        ),
        Product(
            id = "p9",
            sku = "Q-DEGREASE-200L",
            name = "DEGREASE PRO Heavy Duty Alkaline Cleaner",
            subName = "Industrial Grade Degreaser for Workshops & Machinery",
            category = ProductCategory.INDUSTRIAL,
            size = "200L Steel/HDPE Barrel",
            weightKg = 215.0,
            b2cPrice = 14500.0,
            b2bPrice = 11800.0,
            dealerPrice = 9600.0,
            competitorBrand = "Henkel / 3M Industrial 200L",
            competitorPrice = 21000.0,
            cashBackQuantumCash = 1200.0,
            drawableRes = R.drawable.banner_industrial,
            description = "Heavy alkaline formulation for engine degreasing, factory floors, oil spills, and heavy machinery.",
            dilutionRatio = "1:10 for engine degreasing, 1:40 for factory scrubbers",
            features = listOf("Rapid Grease Breakdown", "Low-Foam Scrubber Compatible", "Readily Biodegradable", "Available in 20L, 200L, 1000L IBC")
        ),
        Product(
            id = "p10",
            sku = "Q-OEM-CONTRACT-MFG",
            name = "OEM Contract Chemical Formulation",
            subName = "Private Labeling & Custom Packaging Services",
            category = ProductCategory.OEM_CONTRACT,
            size = "Per Metric Ton / Batch",
            weightKg = 1000.0,
            b2cPrice = 45000.0,
            b2bPrice = 38000.0,
            dealerPrice = 32000.0,
            competitorBrand = "Standard Third Party Chem Plants",
            competitorPrice = 58000.0,
            cashBackQuantumCash = 3000.0,
            drawableRes = R.drawable.banner_oem,
            description = "ISO 9001 certified manufacturing for private label cleaning brands, custom formulations, and contract bottle packing.",
            dilutionRatio = "Custom tailored specifications",
            features = listOf("In-house R&D Lab Testing", "Private Label Screen Printing", "ISO 9001 & GMP Certified", "Flexible Batch Sizes from 1,000L")
        )
    )

    fun getInitialDealerInventory(): List<DealerInventoryItem> = listOf(
        DealerInventoryItem(products[0], stockQty = 14, avgDailySales = 6.2, safetyStockDays = 3), // 2.2 days -> Critical Low!
        DealerInventoryItem(products[1], stockQty = 42, avgDailySales = 4.5, safetyStockDays = 3),
        DealerInventoryItem(products[2], stockQty = 8, avgDailySales = 4.0, safetyStockDays = 3),  // 2.0 days -> Critical Low!
        DealerInventoryItem(products[3], stockQty = 35, avgDailySales = 5.0, safetyStockDays = 3),
        DealerInventoryItem(products[4], stockQty = 11, avgDailySales = 5.2, safetyStockDays = 3), // 2.1 days -> Critical Low!
        DealerInventoryItem(products[5], stockQty = 60, avgDailySales = 3.8, safetyStockDays = 3),
        DealerInventoryItem(products[6], stockQty = 24, avgDailySales = 2.0, safetyStockDays = 3),
        DealerInventoryItem(products[7], stockQty = 18, avgDailySales = 4.1, safetyStockDays = 3),
        DealerInventoryItem(products[8], stockQty = 2, avgDailySales = 0.8, safetyStockDays = 3)
    )

    fun getDealers(): List<GeoDealer> = listOf(
        GeoDealer("d1", "Quantum Hub - Koramangala", "+919885031431", "Bengaluru", "560034", distanceKm = 3.2, stockAvailable = true),
        GeoDealer("d2", "Quantum Station - Whitefield", "+919885031432", "Bengaluru", "560066", distanceKm = 8.5, stockAvailable = true),
        GeoDealer("d3", "Quantum Depot - Peenya Industrial", "+919885031433", "Bengaluru", "560058", distanceKm = 16.4, stockAvailable = true),
        GeoDealer("d4", "Quantum Express - Electronic City", "+919885031434", "Bengaluru", "560100", distanceKm = 24.0, stockAvailable = true),
        GeoDealer("d5", "Quantum Central Factory Warehouse", "+919885031430", "Bengaluru Rural", "562123", distanceKm = 48.0, stockAvailable = true)
    )

    fun getInitialDispatches(): List<LogisticsDispatch> = listOf(
        LogisticsDispatch("ORD-8921", "Hotel Royal Orchid", "560008", weightKg = 120.0, carrier = "Porter API", vehicleType = "3-Wheeler Mini Truck", status = "Driver Assigned", trackingCode = "POR-BLR-4892"),
        LogisticsDispatch("ORD-8922", "Dr. Mehta Clinic", "560034", weightKg = 10.4, carrier = "Dunzo Express", vehicleType = "Two-Wheeler Partner", status = "Out for Delivery", trackingCode = "DNZ-EX-1029"),
        LogisticsDispatch("ORD-8923", "Apex Facility Solutions", "560066", weightKg = 430.0, carrier = "Porter API", vehicleType = "Tata 407 Heavy Truck", status = "Warehouse Loading", trackingCode = "POR-HVY-9930"),
        LogisticsDispatch("ORD-8924", "Ananya Sharma", "560076", weightKg = 7.3, carrier = "Rapido Hyperlocal", vehicleType = "Two-Wheeler Partner", status = "Dispatched", trackingCode = "RPD-HL-5521")
    )

    fun getInitialDealerAds(): List<DealerAdCampaign> = listOf(
        DealerAdCampaign("AD-101", "Koramangala 5km - Degrease Pro Workshop Blast", targetRadiusKm = 5, dailyBudgetInr = 500, status = "Active", leadsPings = 48, clicks = 312),
        DealerAdCampaign("AD-102", "Whitefield Villas - StainOff & Descale Bundle", targetRadiusKm = 7, dailyBudgetInr = 750, status = "Active", leadsPings = 64, clicks = 480),
        DealerAdCampaign("AD-103", "Electronic City Tech Parks - B2B Washroom Refills", targetRadiusKm = 10, dailyBudgetInr = 1200, status = "Optimizing", leadsPings = 82, clicks = 650)
    )

    fun getInitialLegalConsents(): List<LegalConsentRecord> = listOf(
        LegalConsentRecord("LOG-001", "Terms & Conditions", "v2.4 (2026)", "Customer", "152.57.20.14", "Today, 10:45 AM", "Verified"),
        LegalConsentRecord("LOG-002", "B2B Supply & Refund Agreement", "v3.1 (2026)", "Dealer", "49.207.181.9", "Today, 09:12 AM", "Verified"),
        LegalConsentRecord("LOG-003", "Privacy Policy & GST Consent", "v1.8 (2026)", "Customer", "106.51.78.22", "Yesterday, 04:30 PM", "Verified")
    )
}
