package com.example.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.QuantumNavy
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun LegalPolicyViewerModal(
    isOpen: Boolean,
    policyTitle: String,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    val content = when (policyTitle) {
        "Privacy Policy" -> """
            QUANTUM CLEAN SOLUTIONS - PRIVACY & DATA PROTECTION POLICY (2026)
            
            1. Collection of User & Institutional Data:
            We collect contact information (phone, WhatsApp number, name), GSTIN numbers for business entities, and delivery coordinates to route orders to local authorized dealers.
            
            2. Real-Time Geolocation Usage:
            Delivery addresses and pincodes are strictly utilized by our Dynamic Geo-Connect Matrix to identify the nearest stocking dealer. Geolocation data is never shared with unauthorized third parties.
            
            3. WhatsApp Integration & Notifications:
            By initiating checkout, you consent to receive transactional confirmations, digital tax invoices, and automated delivery tracking links via the WhatsApp Cloud API.
            
            4. Data Retention & Compliance:
            Consent logs are stored with cryptographic timestamps and IP address hashes for 7 years pursuant to Indian digital commerce and GST audit regulations.
        """.trimIndent()
        "Return & Refund Agreement" -> """
            QUANTUM CLEAN SOLUTIONS - B2B & CONSUMER RETURN POLICY (2026)
            
            1. Chemical Sealed Container Guarantee:
            All chemicals (STAINOFF, FLUSH MAX, DE-SCALE PRO, HYGIENIX, AROMIST) are delivered in factory-sealed containers with induction seals.
            
            2. Damaged or Leaking Shipments:
            If a container leaks during Porter / Dunzo transit, report within 24 hours of delivery. A replacement will be auto-dispatched from the regional hub at zero additional charge.
            
            3. B2B & Bulk Drum Exchanges:
            Industrial drums (200L / 1000L IBC) are non-refundable once the tamper-evident factory valve is opened, unless chemical lab analysis indicates deviation from guaranteed formulation specs.
            
            4. Quantum Cash Lock-in:
            Quantum Cash earned on orders is strictly applicable as a factory discount on future product purchases and is non-redeemable as cash bank withdrawals.
        """.trimIndent()
        else -> """
            QUANTUM CLEAN SOLUTIONS - MASTER TERMS & CONDITIONS (2026)
            
            1. Ordering & Contract Formation:
            Orders placed via App Voice AI, Catalog UI, or WhatsApp Bot constitute a binding purchase agreement once confirmed by the assigned regional dealer or central factory.
            
            2. Dynamic Lead Routing:
            Customer orders are dispatched from the closest stocking dealer within a 10km to 50km dynamic radius. If stock is unavailable locally, fulfillment occurs directly from the central plant.
            
            3. Pricing & Competitor Comparison:
            Competitor price comparisons displayed on the catalog are benchmarks derived from verifiable market listings and updated via our automated price matrix.
            
            4. Single-Handed Maintenance & Zero-Downtime:
            System operates 24/7 with automated dispatching, dealer restock triggers, and WhatsApp conversational AI order routing.
        """.trimIndent()
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = policyTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = QuantumNavy
                    )
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = content,
                        fontSize = 12.sp,
                        lineHeight = 18.sp,
                        color = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = QuantumNavy),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close & Acknowledge", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
