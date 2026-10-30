package com.example.model

enum class UserRole(val label: String) {
    CUSTOMER("Customer"),
    DEALER("Dealer"),
    SUPER_ADMIN("Super Admin")
}

enum class ProductCategory(val label: String) {
    ALL("All Products"),
    DOMESTIC("Domestic"),
    B2B("B2B Commercial"),
    INDUSTRIAL("Industrial"),
    OEM_CONTRACT("OEM / Contract")
}

data class Product(
    val id: String,
    val sku: String,
    val name: String,
    val subName: String,
    val category: ProductCategory,
    val size: String,
    val weightKg: Double,
    val b2cPrice: Double,
    val b2bPrice: Double,
    val dealerPrice: Double,
    val competitorBrand: String,
    val competitorPrice: Double,
    val cashBackQuantumCash: Double,
    val drawableRes: Int,
    val description: String,
    val dilutionRatio: String,
    val features: List<String>
) {
    val savingsAmount: Double get() = (competitorPrice - b2cPrice).coerceAtLeast(0.0)
    val savingsPercent: Int get() = if (competitorPrice > 0) ((savingsAmount / competitorPrice) * 100).toInt() else 0
}

data class CartItem(
    val product: Product,
    var quantity: Int
)

data class DealerInventoryItem(
    val product: Product,
    val stockQty: Int,
    val avgDailySales: Double,
    val safetyStockDays: Int = 3
) {
    val daysOfCover: Double get() = if (avgDailySales > 0) stockQty / avgDailySales else 999.0
    val isCriticalLow: Boolean get() = daysOfCover <= safetyStockDays
    val suggestedRestockQty: Int get() = ((avgDailySales * 14) - stockQty).toInt().coerceAtLeast(20)
}

data class GeoDealer(
    val id: String,
    val name: String,
    val whatsapp: String,
    val city: String,
    val pincode: String,
    val distanceKm: Double,
    val stockAvailable: Boolean,
    val rating: Double = 4.9
)

data class LogisticsDispatch(
    val orderId: String,
    val customerName: String,
    val destinationPincode: String,
    val weightKg: Double,
    val carrier: String, // "Porter" or "Dunzo"
    val vehicleType: String,
    val status: String,
    val trackingCode: String
)

data class DealerAdCampaign(
    val id: String,
    val title: String,
    val targetRadiusKm: Int,
    val dailyBudgetInr: Int,
    val status: String,
    val leadsPings: Int,
    val clicks: Int
)

data class LegalConsentRecord(
    val id: String,
    val policyType: String,
    val version: String,
    val userRole: String,
    val ipAddress: String,
    val timestamp: String,
    val status: String
)
