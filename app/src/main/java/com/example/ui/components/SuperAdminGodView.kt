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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.LegalConsentRecord
import com.example.model.LogisticsDispatch
import com.example.ui.theme.*

@Composable
fun SuperAdminGodView(
    totalOrders: Int,
    dispatches: List<LogisticsDispatch>,
    legalConsents: List<LegalConsentRecord>,
    isScrapingRunning: Boolean,
    lastScrapedStatus: String,
    onTriggerScraper: () -> Unit,
    onOpenLegalPolicy: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var adminTab by remember { mutableStateOf(0) } // 0: Telemetry, 1: Competitor Scraper, 2: Supabase SQL & Schema, 3: Legal & Audit

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SurfaceLight)
    ) {
        // God-View Top Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = QuantumNavy,
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Super Admin God-View",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Zero-Maintenance Automated Operations Hub",
                            fontSize = 11.sp,
                            color = QuantumMint
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color.White.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "Founder Console",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val tabs = listOf("Telemetry", "Price Scraper", "Supabase SQL", "Legal Consents")
                TabRow(
                    selectedTabIndex = adminTab,
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ) {
                    tabs.forEachIndexed { idx, title ->
                        Tab(
                            selected = adminTab == idx,
                            onClick = { adminTab = idx },
                            text = { Text(title, fontSize = 11.sp, fontWeight = if (adminTab == idx) FontWeight.Bold else FontWeight.Normal) }
                        )
                    }
                }
            }
        }

        when (adminTab) {
            0 -> AdminTelemetryTab(totalOrders, dispatches)
            1 -> CompetitorScraperTab(isScrapingRunning, lastScrapedStatus, onTriggerScraper)
            2 -> SupabaseSqlViewerTab()
            3 -> LegalComplianceAuditTab(legalConsents, onOpenLegalPolicy)
        }
    }
}

@Composable
private fun AdminTelemetryTab(totalOrders: Int, dispatches: List<LogisticsDispatch>) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            // KPI Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                KpiMetricCard("Monthly GMV", "₹14,82,500", "+22.4% vs last mo", QuantumEmerald, Modifier.weight(1f))
                KpiMetricCard("AI Voice Orders", "64.8%", "Whisper + WhatsApp", QuantumBlue, Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                KpiMetricCard("Active Dealers", "48 Hubs", "Dynamic Radius Avg 14km", QuantumNavy, Modifier.weight(1f))
                KpiMetricCard("Quantum Cash Held", "₹1,84,200", "Strict Factory Discount", QuantumAmber, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Automated Logistics Fleet Status", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = QuantumNavy)
            Spacer(modifier = Modifier.height(8.dp))
        }

        items(dispatches) { dispatch ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "${dispatch.orderId} • ${dispatch.carrier}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(text = "Weight: ${dispatch.weightKg} kg • Tracking: ${dispatch.trackingCode}", fontSize = 11.sp, color = TextSecondary)
                    }
                    Surface(shape = RoundedCornerShape(6.dp), color = QuantumLightGreen) {
                        Text(
                            text = dispatch.status,
                            color = QuantumEmerald,
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
private fun KpiMetricCard(title: String, value: String, sub: String, tint: Color, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(text = title, fontSize = 11.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Black, color = tint)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = sub, fontSize = 10.sp, color = TextMuted)
        }
    }
}

@Composable
private fun CompetitorScraperTab(
    isRunning: Boolean,
    statusText: String,
    onTriggerScraper: () -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Automated Competitor Price Scraper",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = QuantumNavy
                    )
                    Text(
                        text = "Scrapes competitor market listings ethically respecting robots.txt and rate-limits to auto-update the 'Best Price' comparison matrix.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isRunning) Color(0xFFEFF6FF) else Color(0xFFF0FDF4),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isRunning) Color(0xFFBFDBFE) else Color(0xFFBBF7D0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            if (isRunning) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = QuantumBlue)
                            } else {
                                Icon(Icons.Default.CheckCircle, contentDescription = "OK", tint = QuantumEmerald, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = statusText, fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onTriggerScraper,
                        enabled = !isRunning,
                        colors = ButtonDefaults.buttonColors(containerColor = QuantumNavy),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isRunning) "Scraping & Parsing..." else "Run Scheduled Scraping Job Now", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text("Tracked Competitor Benchmarks", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = QuantumNavy)
            Spacer(modifier = Modifier.height(6.dp))

            val comparisons = listOf(
                Triple("Ariel / Surf Excel Matic 5L", "₹850.00", "Quantum: ₹499 (Save 41%)"),
                Triple("Harpic Power Plus 5L", "₹720.00", "Quantum: ₹399 (Save 44%)"),
                Triple("Colin Glass Cleaner 5L", "₹580.00", "Quantum: ₹340 (Save 41%)"),
                Triple("Henkel Heavy Degreaser 200L", "₹21,000.00", "Quantum: ₹14,500 (Save 31%)")
            )

            comparisons.forEach { (brand, compPrice, quantumAdv) ->
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = brand, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = "Market Price: $compPrice", fontSize = 11.sp, color = TextMuted)
                        }
                        Surface(shape = RoundedCornerShape(6.dp), color = QuantumLightGreen) {
                            Text(
                                text = quantumAdv,
                                color = QuantumEmerald,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
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
private fun SupabaseSqlViewerTab() {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Storage, contentDescription = "SQL", tint = QuantumNavy, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Supabase / PostgreSQL 15 Architecture", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = QuantumNavy)
                    }
                    Text(
                        text = "Production schema with Row Level Security (RLS), PostGIS coordinates, and Wallet check constraints.",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val tables = listOf(
                        "users (id, phone, full_name, role, wallet_balance, gstin)",
                        "dealers (id, store_name, location GEOGRAPHY, service_radius_km)",
                        "products (id, sku, b2c_price, b2b_price, competitor_price)",
                        "dealer_inventory (dealer_id, product_id, stock_qty, safety_stock_days)",
                        "orders (id, customer_id, dealer_id, total, assigned_carrier)",
                        "wallet_transactions (id, user_id, amount, tx_type)",
                        "dealer_ads (id, dealer_id, meta_campaign_id, budget, leads)",
                        "legal_consents (id, user_id, policy_type, version, ip_address)"
                    )

                    tables.forEach { tableDef ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF1E293B),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                        ) {
                            Text(
                                text = tableDef,
                                color = Color(0xFF38BDF8),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LegalComplianceAuditTab(
    consents: List<LegalConsentRecord>,
    onOpenLegalPolicy: (String) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("Legal, Compliance & Policy Engine", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = QuantumNavy)
                    Text("Immutable audit trail of customer and dealer consents for dispute resolution & GST compliance.", fontSize = 11.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { onOpenLegalPolicy("Terms & Conditions") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = QuantumNavy),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Text("View T&C", fontSize = 11.sp)
                        }
                        Button(
                            onClick = { onOpenLegalPolicy("Privacy Policy") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = QuantumNavy),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Text("Privacy Policy", fontSize = 11.sp)
                        }
                        Button(
                            onClick = { onOpenLegalPolicy("Return & Refund Agreement") },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = QuantumNavy),
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Text("Refund Policy", fontSize = 11.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text("Live Consent Audit Log", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = QuantumNavy)
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(consents) { log ->
            Card(
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${log.id} • ${log.policyType}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Surface(shape = RoundedCornerShape(4.dp), color = QuantumLightGreen) {
                            Text(text = log.status, color = QuantumEmerald, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = "Version: ${log.version} • Role: ${log.userRole} • IP: ${log.ipAddress} • ${log.timestamp}",
                        fontSize = 11.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
