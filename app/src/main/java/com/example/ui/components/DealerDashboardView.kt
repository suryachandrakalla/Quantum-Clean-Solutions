package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.theme.*

@Composable
fun DealerDashboardView(
    inventory: List<DealerInventoryItem>,
    dealers: List<GeoDealer>,
    dynamicRadiusKm: Int,
    dealerAds: List<DealerAdCampaign>,
    dispatches: List<LogisticsDispatch>,
    restockNotice: String?,
    onSendRestockWhatsApp: (DealerInventoryItem) -> Unit,
    onUpdateRadius: (Int) -> Unit,
    onLaunchAd: (Product, budget: Int, radius: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Inventory & Alerts, 1: Geo Radar, 2: Ad Marketing, 3: Logistics

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceLight)
    ) {
        // Dealer Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Dealer Hub - Koramangala Station",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = QuantumNavy
                        )
                        Text(
                            text = "GSTIN: 29AABCQ8492K1Z9 • Active Dynamic Radius: ${dynamicRadiusKm}km",
                            fontSize = 11.sp,
                            color = TextMuted
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = QuantumLightGreen,
                        border = androidx.compose.foundation.BorderStroke(1.dp, QuantumMint)
                    ) {
                        Text(
                            text = "Verified Dealer",
                            color = QuantumEmerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Restock Notification Banner
                if (restockNotice != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFEFF6FF),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Sent", tint = QuantumBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = restockNotice, fontSize = 11.sp, color = QuantumNavy, fontWeight = FontWeight.SemiBold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Sub Tabs: Inventory, Geo-Radar, Micro-Ads, Dispatches
                val tabs = listOf("Inventory & Alerts", "Geo Radar", "Micro-Ads", "Logistics")
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = QuantumNavy
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(title, fontSize = 11.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal) }
                        )
                    }
                }
            }
        }

        when (selectedTab) {
            0 -> InventoryAndAlertsTab(inventory, onSendRestockWhatsApp)
            1 -> GeoRadarTab(dealers, dynamicRadiusKm, onUpdateRadius)
            2 -> MicroTargetedAdsTab(dealerAds, onLaunchAd)
            3 -> LogisticsDispatchesTab(dispatches)
        }
    }
}

@Composable
private fun InventoryAndAlertsTab(
    inventory: List<DealerInventoryItem>,
    onSendRestockWhatsApp: (DealerInventoryItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            // Predictive Forecast Alert Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFFFBEB),
                border = androidx.compose.foundation.BorderStroke(1.dp, QuantumAmberLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.WarningAmber, contentDescription = "Alert", tint = QuantumAmber, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "AI Predictive Stock Forecaster",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = QuantumAmber
                        )
                        Text(
                            text = "Items with ≤ 3 days projected sales trigger automated WhatsApp Restock POs to Quantum Factory.",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        items(inventory) { item ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.product.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = TextPrimary
                            )
                            if (item.isCriticalLow) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = QuantumRedLight
                                ) {
                                    Text(
                                        text = "CRITICAL LOW",
                                        color = QuantumRed,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "Stock: ${item.stockQty} units • Velocity: ${item.avgDailySales}/day • Cover: String.format(%.1f, item.daysOfCover) days",
                            fontSize = 11.sp,
                            color = if (item.isCriticalLow) QuantumRed else TextSecondary
                        )
                    }

                    if (item.isCriticalLow) {
                        Button(
                            onClick = { onSendRestockWhatsApp(item) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = "WhatsApp", tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Restock PO", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = QuantumLightGreen
                        ) {
                            Text(
                                text = "Healthy Stock",
                                color = QuantumEmerald,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GeoRadarTab(
    dealers: List<GeoDealer>,
    dynamicRadiusKm: Int,
    onUpdateRadius: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Dynamic Geo-Connect Matrix (Lead Routing)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = QuantumNavy
                    )
                    Text(
                        text = "Orders auto-route to the nearest stocking dealer. Radius dynamically expands by +10km if stock is unavailable.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Current Service Radius: ${dynamicRadiusKm} km",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = QuantumEmerald
                    )
                    Slider(
                        value = dynamicRadiusKm.toFloat(),
                        onValueChange = { onUpdateRadius(it.toInt()) },
                        valueRange = 5f..50f,
                        steps = 8,
                        colors = SliderDefaults.colors(thumbColor = QuantumEmerald, activeTrackColor = QuantumMint)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text("Regional Hubs in Dynamic Matrix", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = QuantumNavy)
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(dealers) { dealer ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = dealer.name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        Text(text = "${dealer.city} (${dealer.pincode}) • ${dealer.distanceKm} km away", fontSize = 11.sp, color = TextSecondary)
                    }
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (dealer.distanceKm <= dynamicRadiusKm) QuantumLightGreen else Color(0xFFF1F5F9)
                    ) {
                        Text(
                            text = if (dealer.distanceKm <= dynamicRadiusKm) "Within Radius" else "Standby Hub",
                            color = if (dealer.distanceKm <= dynamicRadiusKm) QuantumEmerald else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MicroTargetedAdsTab(
    ads: List<DealerAdCampaign>,
    onLaunchAd: (Product, budget: Int, radius: Int) -> Unit
) {
    var budget by remember { mutableStateOf(500) }
    var radius by remember { mutableStateOf(5) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Launch Micro-Targeted Meta Ads",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = QuantumNavy
                    )
                    Text(
                        text = "Automated FB/Instagram campaigns geo-fenced around your shop. Leads click directly into your WhatsApp.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Daily Ad Budget: ₹$budget", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("Geo Radius: ${radius}km", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    Slider(
                        value = budget.toFloat(),
                        onValueChange = { budget = it.toInt() },
                        valueRange = 300f..2000f,
                        steps = 16,
                        colors = SliderDefaults.colors(thumbColor = QuantumBlue, activeTrackColor = QuantumLightBlue)
                    )

                    Button(
                        onClick = {
                            val sampleProd = com.example.data.QuantumCatalogData.products[0]
                            onLaunchAd(sampleProd, budget, radius)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = QuantumBlue),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Campaign, contentDescription = "Ads", tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Deploy Campaign via Meta Graph API", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text("Active Dealer Campaigns", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = QuantumNavy)
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(ads) { ad ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = ad.title, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        Surface(shape = RoundedCornerShape(6.dp), color = QuantumLightGreen) {
                            Text(text = ad.status, color = QuantumEmerald, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Budget: ₹${ad.dailyBudgetInr}/day • Radius: ${ad.targetRadiusKm}km • Clicks: ${ad.clicks} • WhatsApp Leads: ${ad.leadsPings}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun LogisticsDispatchesTab(dispatches: List<LogisticsDispatch>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF1F5F9),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocalShipping, contentDescription = "Logistics", tint = QuantumNavy, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text("Multi-Partner Dispatch Engine", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = QuantumNavy)
                        Text("Orders ≥ 30kg auto-routed to Porter API (Mini Truck). Domestic refills routed to Dunzo/Rapido.", fontSize = 11.sp, color = TextSecondary)
                    }
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
        }

        items(dispatches) { item ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${item.orderId} • ${item.customerName}", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = TextPrimary)
                        Surface(shape = RoundedCornerShape(6.dp), color = if (item.carrier.contains("Porter")) Color(0xFFFEF3C7) else QuantumLightGreen) {
                            Text(
                                text = item.carrier,
                                color = if (item.carrier.contains("Porter")) QuantumAmber else QuantumEmerald,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Weight: ${item.weightKg} kg • Vehicle: ${item.vehicleType} • Code: ${item.trackingCode}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Status: ${item.status}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = QuantumBlue
                    )
                }
            }
        }
    }
}
