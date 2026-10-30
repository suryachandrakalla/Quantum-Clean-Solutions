package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.model.ProductCategory
import com.example.model.UserRole
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuantumCleanTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    QuantumCleanApp(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun QuantumCleanApp(
    viewModel: MainViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceLight)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Navigation & 3-in-1 Role Switcher
            TopRoleNavigationBar(
                currentRole = uiState.currentRole,
                onRoleSelected = { viewModel.switchRole(it) },
                cartItemCount = uiState.cartTotalCount,
                quantumCash = uiState.quantumCashWallet,
                onCartClick = { viewModel.openCheckoutModal() },
                onVoiceClick = { viewModel.openVoiceModal() }
            )

            // Dynamic Body by Role
            Box(modifier = Modifier.weight(1f)) {
                when (uiState.currentRole) {
                    UserRole.CUSTOMER -> {
                        CustomerMegaStoreView(
                            uiState = uiState,
                            onCategorySelect = { viewModel.selectCategory(it) },
                            onSearchChange = { viewModel.updateSearchQuery(it) },
                            onAddToCart = { viewModel.addToCart(it) },
                            onOpenVoice = { viewModel.openVoiceModal() }
                        )
                    }
                    UserRole.DEALER -> {
                        DealerDashboardView(
                            inventory = uiState.dealerInventory,
                            dealers = uiState.dealers,
                            dynamicRadiusKm = uiState.dynamicRadiusKm,
                            dealerAds = uiState.dealerAds,
                            dispatches = uiState.dispatches,
                            restockNotice = uiState.restockNoticeMessage,
                            onSendRestockWhatsApp = { viewModel.sendRestockAlertToWhatsApp(it) },
                            onUpdateRadius = { viewModel.updateDynamicRadius(it) },
                            onLaunchAd = { prod, budget, rad -> viewModel.launchDealerAd(prod, budget, rad) }
                        )
                    }
                    UserRole.SUPER_ADMIN -> {
                        SuperAdminGodView(
                            totalOrders = uiState.dispatches.size,
                            dispatches = uiState.dispatches,
                            legalConsents = uiState.legalConsents,
                            isScrapingRunning = uiState.isScrapingRunning,
                            lastScrapedStatus = uiState.lastScrapedStatus,
                            onTriggerScraper = { viewModel.triggerCompetitorPriceScraper() },
                            onOpenLegalPolicy = { viewModel.openLegalModal(it) }
                        )
                    }
                }
            }
        }

        // Floating Cart Summary Bar (When Customer has items)
        if (uiState.currentRole == UserRole.CUSTOMER && uiState.cartTotalCount > 0) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .testTag("floating_cart_bar"),
                shape = RoundedCornerShape(16.dp),
                color = QuantumNavy,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${uiState.cartTotalCount} items",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "• Save ₹${uiState.cartTotalSavings.toInt()}",
                                color = QuantumMint,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "Payable: ₹${uiState.finalPayable.toInt()}",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp
                        )
                    }

                    Button(
                        onClick = { viewModel.openCheckoutModal() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Chat,
                            contentDescription = "WhatsApp Order",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "WhatsApp Order",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }

        // Voice AI Modal
        VoiceOrderAiModal(
            isOpen = uiState.isVoiceModalOpen,
            transcript = uiState.voiceTranscript,
            isProcessing = uiState.isVoiceProcessing,
            statusMessage = uiState.voiceParsedMessage,
            onSimulateSpeech = { viewModel.simulateVoiceOrder(it) },
            onDismiss = { viewModel.closeVoiceModal() }
        )

        // WhatsApp Checkout Modal
        WhatsAppCheckoutDialog(
            isOpen = uiState.isCheckoutModalOpen,
            cartItems = uiState.cartItems.values.toList(),
            cartSubtotal = uiState.cartSubtotal,
            cartSavings = uiState.cartTotalSavings,
            quantumCashWallet = uiState.quantumCashWallet,
            applyQuantumCash = uiState.applyQuantumCash,
            appliedCashDeduction = uiState.appliedCashDeduction,
            finalPayable = uiState.finalPayable,
            earnedCash = uiState.earnedQuantumCashThisOrder,
            selectedPaymentMethod = uiState.selectedPaymentMethod,
            successNotice = uiState.checkoutSuccessNotice,
            onToggleQuantumCash = { viewModel.toggleQuantumCash(it) },
            onSelectPaymentMethod = { viewModel.setPaymentMethod(it) },
            onConfirmCheckout = { addr, ph -> viewModel.processWhatsAppCheckout(addr, ph) },
            onDismiss = { viewModel.closeCheckoutModal() }
        )

        // Legal Policy Modal
        LegalPolicyViewerModal(
            isOpen = uiState.isLegalModalOpen,
            policyTitle = uiState.activeLegalPolicy,
            onDismiss = { viewModel.closeLegalModal() }
        )
    }
}

@Composable
fun CustomerMegaStoreView(
    uiState: com.example.viewmodel.QuantumUiState,
    onCategorySelect: (ProductCategory) -> Unit,
    onSearchChange: (String) -> Unit,
    onAddToCart: (com.example.model.Product) -> Unit,
    onOpenVoice: () -> Unit
) {
    val filteredProducts = remember(uiState.products, uiState.selectedCategory, uiState.searchQuery) {
        uiState.products.filter { prod ->
            val matchCategory = uiState.selectedCategory == ProductCategory.ALL || prod.category == uiState.selectedCategory
            val matchSearch = uiState.searchQuery.isBlank() ||
                    prod.name.contains(uiState.searchQuery, ignoreCase = true) ||
                    prod.subName.contains(uiState.searchQuery, ignoreCase = true) ||
                    prod.features.any { it.contains(uiState.searchQuery, ignoreCase = true) }
            matchCategory && matchSearch
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = if (uiState.cartTotalCount > 0) 80.dp else 0.dp)
    ) {
        // Hero Brand Banner & Search
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    Image(
                        painter = painterResource(id = R.drawable.banner_cleaning),
                        contentDescription = "Quantum Banner",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                    )

                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "ISO 9001 & GMP Certified",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = QuantumEmerald
                                )
                                Text(
                                    text = "Direct Factory Formulations • Save up to 45%",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = QuantumLightBlue
                            ) {
                                Text(
                                    text = "Made in India 🇮🇳",
                                    color = QuantumNavy,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Search Input with Voice Icon
                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = onSearchChange,
                            placeholder = { Text("Search detergent, degreaser, toilet gel...", fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = TextMuted) },
                            trailingIcon = {
                                IconButton(onClick = onOpenVoice) {
                                    Icon(Icons.Default.Mic, contentDescription = "Voice Search", tint = QuantumMint)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("search_catalog_input"),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )
                    }
                }
            }

            // Category Filter Pills
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(ProductCategory.values()) { cat ->
                    val isSelected = uiState.selectedCategory == cat
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) QuantumNavy else Color.White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) QuantumNavy else DividerColor
                        ),
                        modifier = Modifier
                            .clickable { onCategorySelect(cat) }
                            .testTag("category_pill_${cat.name.lowercase()}")
                    ) {
                        Text(
                            text = cat.label,
                            color = if (isSelected) Color.White else TextPrimary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }

        // Product Catalog Cards
        items(filteredProducts) { product ->
            Box(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                ProductCatalogCard(
                    product = product,
                    userRole = uiState.currentRole,
                    onAddToCart = onAddToCart
                )
            }
        }
    }
}
