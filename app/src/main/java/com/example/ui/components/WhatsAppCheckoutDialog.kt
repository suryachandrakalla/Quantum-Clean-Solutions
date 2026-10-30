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
import androidx.compose.ui.window.Dialog
import com.example.model.CartItem
import com.example.ui.theme.*

@Composable
fun WhatsAppCheckoutDialog(
    isOpen: Boolean,
    cartItems: List<CartItem>,
    cartSubtotal: Double,
    cartSavings: Double,
    quantumCashWallet: Double,
    applyQuantumCash: Boolean,
    appliedCashDeduction: Double,
    finalPayable: Double,
    earnedCash: Double,
    selectedPaymentMethod: String,
    successNotice: String?,
    onToggleQuantumCash: (Boolean) -> Unit,
    onSelectPaymentMethod: (String) -> Unit,
    onConfirmCheckout: (address: String, phone: String) -> Unit,
    onDismiss: () -> Unit
) {
    if (!isOpen) return

    var deliveryAddress by remember { mutableStateOf("Hotel Grand Horizon, 14th Main, Indiranagar, Bengaluru - 560038") }
    var phoneNumber by remember { mutableStateOf("+91 98850 31431") }
    var consentAccepted by remember { mutableStateOf(true) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .testTag("whatsapp_checkout_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(18.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF25D366)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "WhatsApp",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "WhatsApp Direct Checkout",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = QuantumNavy
                            )
                            Text(
                                text = "Automated Dispatch & Wallet Deductions",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.size(28.dp)) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (successNotice != null) {
                    // Success View
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(QuantumLightGreen),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Success",
                                tint = QuantumEmerald,
                                modifier = Modifier.size(40.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Order Successfully Placed!",
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp,
                            color = QuantumNavy
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = successNotice,
                            fontSize = 13.sp,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = QuantumNavy),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Done & Return to Store", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Checkout Details Form
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        item {
                            // Order Summary Section
                            Text("Order Items (${cartItems.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = QuantumNavy)
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        items(cartItems) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${item.product.name} (${item.product.size})",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = "Qty: ${item.quantity} × ₹${item.product.b2cPrice.toInt()}",
                                        fontSize = 11.sp,
                                        color = TextMuted
                                    )
                                }
                                Text(
                                    text = "₹${(item.product.b2cPrice * item.quantity).toInt()}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = QuantumNavy
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = DividerColor)
                            Spacer(modifier = Modifier.height(12.dp))

                            // Quantum Cash Wallet Deduction Switch
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF0FDF4),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Savings,
                                            contentDescription = "Wallet",
                                            tint = QuantumEmerald,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = "Apply Quantum Cash",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = QuantumNavy
                                            )
                                            Text(
                                                text = "Available: ₹${quantumCashWallet.toInt()} (Save ₹${appliedCashDeduction.toInt()})",
                                                fontSize = 11.sp,
                                                color = QuantumEmerald,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                    Switch(
                                        checked = applyQuantumCash,
                                        onCheckedChange = onToggleQuantumCash,
                                        colors = SwitchDefaults.colors(checkedThumbColor = QuantumEmerald, checkedTrackColor = Color(0xFF86EFAC))
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Delivery Address & Phone Fields
                            OutlinedTextField(
                                value = deliveryAddress,
                                onValueChange = { deliveryAddress = it },
                                label = { Text("Delivery Address & Pincode", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = phoneNumber,
                                onValueChange = { phoneNumber = it },
                                label = { Text("WhatsApp Contact Number", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp),
                                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Payment Method Selection
                            Text("Payment & Settlement Mode", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = QuantumNavy)
                            Spacer(modifier = Modifier.height(6.dp))

                            val paymentOptions = listOf(
                                "WhatsApp + UPI (Instant 5% Off)",
                                "Razorpay Gateway (Card / NetBanking)",
                                "Cash on Delivery (Verified Dealer Route)"
                            )

                            paymentOptions.forEach { option ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onSelectPaymentMethod(option) }
                                        .padding(vertical = 4.dp)
                                ) {
                                    RadioButton(
                                        selected = selectedPaymentMethod == option,
                                        onClick = { onSelectPaymentMethod(option) },
                                        colors = RadioButtonDefaults.colors(selectedColor = QuantumEmerald)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(text = option, fontSize = 12.sp, color = TextPrimary)
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Legal Consent Checkbox
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { consentAccepted = !consentAccepted }
                            ) {
                                Checkbox(
                                    checked = consentAccepted,
                                    onCheckedChange = { consentAccepted = it },
                                    colors = CheckboxDefaults.colors(checkedColor = QuantumEmerald)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "I accept Terms & Conditions, Privacy Policy, and B2B Exchange Policy.",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Price Calculation Box
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF8FAFC),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Items Subtotal:", fontSize = 12.sp, color = TextSecondary)
                                        Text("₹${cartSubtotal.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                    if (appliedCashDeduction > 0) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text("Quantum Cash Applied:", fontSize = 12.sp, color = QuantumEmerald)
                                            Text("-₹${appliedCashDeduction.toInt()}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = QuantumEmerald)
                                        }
                                    }
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Total Competitor Savings:", fontSize = 12.sp, color = SavingsBadgeGreen)
                                        Text("₹${cartSavings.toInt()} Saved", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SavingsBadgeGreen)
                                    }
                                    Divider(modifier = Modifier.padding(vertical = 6.dp), color = DividerColor)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Final Payable Amount:", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = QuantumNavy)
                                        Text("₹${finalPayable.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Black, color = QuantumEmerald)
                                    }
                                }
                            }
                        }
                    }

                    // Bottom Order Button
                    Button(
                        onClick = { onConfirmCheckout(deliveryAddress, phoneNumber) },
                        enabled = consentAccepted && cartItems.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                            .testTag("submit_whatsapp_order"),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Trigger WhatsApp Order (₹${finalPayable.toInt()})",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
