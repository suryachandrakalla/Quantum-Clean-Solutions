package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.QuantumCatalogData
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuantumUiState(
    val currentRole: UserRole = UserRole.CUSTOMER,
    val selectedCategory: ProductCategory = ProductCategory.ALL,
    val searchQuery: String = "",
    val products: List<Product> = QuantumCatalogData.products,
    val cartItems: Map<String, CartItem> = emptyMap(),
    val quantumCashWallet: Double = 350.0,
    val isVoiceModalOpen: Boolean = false,
    val voiceTranscript: String = "",
    val isVoiceProcessing: Boolean = false,
    val voiceParsedMessage: String = "",
    // Checkout State
    val isCheckoutModalOpen: Boolean = false,
    val applyQuantumCash: Boolean = true,
    val selectedPaymentMethod: String = "WhatsApp + UPI (Instant 5% Off)",
    val checkoutSuccessNotice: String? = null,
    // Dealer State
    val dealerInventory: List<DealerInventoryItem> = QuantumCatalogData.getInitialDealerInventory(),
    val dealers: List<GeoDealer> = QuantumCatalogData.getDealers(),
    val dynamicRadiusKm: Int = 10,
    val restockNoticeMessage: String? = null,
    val dealerAds: List<DealerAdCampaign> = QuantumCatalogData.getInitialDealerAds(),
    val dispatches: List<LogisticsDispatch> = QuantumCatalogData.getInitialDispatches(),
    // Competitor Price Scraper State
    val isScrapingRunning: Boolean = false,
    val lastScrapedStatus: String = "Matrix Live - All prices synced (Robots.txt compliant)",
    // Legal & Policy State
    val legalConsents: List<LegalConsentRecord> = QuantumCatalogData.getInitialLegalConsents(),
    val isLegalModalOpen: Boolean = false,
    val activeLegalPolicy: String = "Terms & Conditions"
) {
    val cartTotalCount: Int get() = cartItems.values.sumOf { it.quantity }
    val cartSubtotal: Double get() = cartItems.values.sumOf {
        val unitPrice = when (currentRole) {
            UserRole.CUSTOMER -> it.product.b2cPrice
            UserRole.DEALER -> it.product.dealerPrice
            UserRole.SUPER_ADMIN -> it.product.b2bPrice
        }
        unitPrice * it.quantity
    }
    val cartCompetitorTotal: Double get() = cartItems.values.sumOf { it.product.competitorPrice * it.quantity }
    val cartTotalSavings: Double get() = (cartCompetitorTotal - cartSubtotal).coerceAtLeast(0.0)
    val appliedCashDeduction: Double get() = if (applyQuantumCash) cartSubtotal.coerceAtMost(quantumCashWallet) else 0.0
    val finalPayable: Double get() = (cartSubtotal - appliedCashDeduction).coerceAtLeast(0.0)
    val earnedQuantumCashThisOrder: Double get() = cartItems.values.sumOf { it.product.cashBackQuantumCash * it.quantity }
}

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(QuantumUiState())
    val uiState: StateFlow<QuantumUiState> = _uiState.asStateFlow()

    fun switchRole(newRole: UserRole) {
        _uiState.update { it.copy(currentRole = newRole) }
    }

    fun selectCategory(category: ProductCategory) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun updateSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun addToCart(product: Product) {
        _uiState.update { state ->
            val updated = state.cartItems.toMutableMap()
            val existing = updated[product.id]
            if (existing != null) {
                updated[product.id] = existing.copy(quantity = existing.quantity + 1)
            } else {
                updated[product.id] = CartItem(product, 1)
            }
            state.copy(cartItems = updated)
        }
    }

    fun removeFromCart(productId: String) {
        _uiState.update { state ->
            val updated = state.cartItems.toMutableMap()
            val existing = updated[productId]
            if (existing != null) {
                if (existing.quantity > 1) {
                    updated[productId] = existing.copy(quantity = existing.quantity - 1)
                } else {
                    updated.remove(productId)
                }
            }
            state.copy(cartItems = updated)
        }
    }

    fun clearCart() {
        _uiState.update { it.copy(cartItems = emptyMap()) }
    }

    fun toggleQuantumCash(apply: Boolean) {
        _uiState.update { it.copy(applyQuantumCash = apply) }
    }

    fun setPaymentMethod(method: String) {
        _uiState.update { it.copy(selectedPaymentMethod = method) }
    }

    fun openVoiceModal() {
        _uiState.update {
            it.copy(
                isVoiceModalOpen = true,
                voiceTranscript = "",
                isVoiceProcessing = false,
                voiceParsedMessage = ""
            )
        }
    }

    fun closeVoiceModal() {
        _uiState.update { it.copy(isVoiceModalOpen = false) }
    }

    fun simulateVoiceOrder(presetSpeech: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    voiceTranscript = presetSpeech,
                    isVoiceProcessing = true,
                    voiceParsedMessage = "Listening to audio & sending to Whisper..."
                )
            }
            delay(1200)
            _uiState.update {
                it.copy(voiceParsedMessage = "NLP intent extraction: matching Quantum SKUs...")
            }
            delay(1000)

            // Parse speech intent into cart items
            val products = QuantumCatalogData.products
            val stainoff = products[0]
            val swipeshine = products[2]
            val flushmax = products[3]
            val degrease = products[8]

            _uiState.update { state ->
                val newCart = state.cartItems.toMutableMap()
                if (presetSpeech.contains("Stainoff", ignoreCase = true) || presetSpeech.contains("Detergent", ignoreCase = true)) {
                    val curr = newCart[stainoff.id]?.quantity ?: 0
                    newCart[stainoff.id] = CartItem(stainoff, curr + 2)
                }
                if (presetSpeech.contains("Dishwash", ignoreCase = true) || presetSpeech.contains("Swipe", ignoreCase = true)) {
                    val curr = newCart[swipeshine.id]?.quantity ?: 0
                    newCart[swipeshine.id] = CartItem(swipeshine, curr + 1)
                }
                if (presetSpeech.contains("Toilet", ignoreCase = true) || presetSpeech.contains("Flush", ignoreCase = true)) {
                    val curr = newCart[flushmax.id]?.quantity ?: 0
                    newCart[flushmax.id] = CartItem(flushmax, curr + 2)
                }
                if (presetSpeech.contains("Degrease", ignoreCase = true) || presetSpeech.contains("Industrial", ignoreCase = true)) {
                    val curr = newCart[degrease.id]?.quantity ?: 0
                    newCart[degrease.id] = CartItem(degrease, curr + 1)
                }
                state.copy(
                    cartItems = newCart,
                    isVoiceProcessing = false,
                    voiceParsedMessage = "Added to Cart! WhatsApp confirmation payload generated."
                )
            }
        }
    }

    fun openCheckoutModal() {
        _uiState.update { it.copy(isCheckoutModalOpen = true, checkoutSuccessNotice = null) }
    }

    fun closeCheckoutModal() {
        _uiState.update { it.copy(isCheckoutModalOpen = false) }
    }

    fun processWhatsAppCheckout(address: String, phone: String) {
        viewModelScope.launch {
            val deduction = _uiState.value.appliedCashDeduction
            val earnedCash = _uiState.value.earnedQuantumCashThisOrder
            val totalWeight = _uiState.value.cartItems.values.sumOf { it.product.weightKg * it.quantity }
            val carrierAssigned = if (totalWeight >= 30.0) "Porter API (Heavy Vehicle)" else "Dunzo Express (Two-Wheeler)"

            // Add new dispatch automatically
            val newDispatch = LogisticsDispatch(
                orderId = "ORD-${(1000..9999).random()}",
                customerName = if (address.isNotBlank()) address.take(18) else "Express Customer",
                destinationPincode = "560034",
                weightKg = totalWeight,
                carrier = carrierAssigned,
                vehicleType = if (totalWeight >= 30.0) "3-Wheeler Mini Truck" else "Two-Wheeler Partner",
                status = "Dispatched via Webhook",
                trackingCode = "Q-EXP-${(10000..99999).random()}"
            )

            // Log legal consent
            val newConsent = LegalConsentRecord(
                id = "LOG-${(100..999).random()}",
                policyType = "Terms & Conditions & Refund Agreement",
                version = "v2.4 (2026)",
                userRole = _uiState.value.currentRole.label,
                ipAddress = "192.168.1.${(10..99).random()}",
                timestamp = "Just now",
                status = "Consented on WhatsApp Checkout"
            )

            _uiState.update { state ->
                val remainingWallet = (state.quantumCashWallet - deduction + earnedCash).coerceAtLeast(0.0)
                state.copy(
                    quantumCashWallet = remainingWallet,
                    cartItems = emptyMap(),
                    dispatches = listOf(newDispatch) + state.dispatches,
                    legalConsents = listOf(newConsent) + state.legalConsents,
                    checkoutSuccessNotice = "WhatsApp order generated! Order confirmed with $carrierAssigned. Wallet credited with ₹${earnedCash.toInt()} Quantum Cash."
                )
            }
        }
    }

    fun sendRestockAlertToWhatsApp(item: DealerInventoryItem) {
        viewModelScope.launch {
            val alert = "WhatsApp Restock PO Sent: ${item.product.name} (Suggested: ${item.suggestedRestockQty} units). Order routed to Quantum Factory."
            _uiState.update { it.copy(restockNoticeMessage = alert) }
            delay(4000)
            _uiState.update { it.copy(restockNoticeMessage = null) }
        }
    }

    fun updateDynamicRadius(newRadiusKm: Int) {
        _uiState.update { it.copy(dynamicRadiusKm = newRadiusKm) }
    }

    fun launchDealerAd(product: Product, budget: Int, radiusKm: Int) {
        val newAd = DealerAdCampaign(
            id = "AD-${(100..999).random()}",
            title = "${product.name} - ${radiusKm}km Radius Blast",
            targetRadiusKm = radiusKm,
            dailyBudgetInr = budget,
            status = "Active",
            leadsPings = (12..35).random(),
            clicks = (140..390).random()
        )
        _uiState.update { it.copy(dealerAds = listOf(newAd) + it.dealerAds) }
    }

    fun triggerCompetitorPriceScraper() {
        viewModelScope.launch {
            _uiState.update { it.copy(isScrapingRunning = true, lastScrapedStatus = "Connecting to competitor sites via Puppeteer/Cheerio...") }
            delay(1500)
            _uiState.update { it.copy(lastScrapedStatus = "Parsing robots.txt & updating Quantum Price Matrix...") }
            delay(1200)
            _uiState.update {
                it.copy(
                    isScrapingRunning = false,
                    lastScrapedStatus = "Completed: 14 Competitor SKUs updated. Quantum maintains 34.2% average savings advantage."
                )
            }
        }
    }

    fun openLegalModal(policyTitle: String) {
        _uiState.update { it.copy(isLegalModalOpen = true, activeLegalPolicy = policyTitle) }
    }

    fun closeLegalModal() {
        _uiState.update { it.copy(isLegalModalOpen = false) }
    }
}
