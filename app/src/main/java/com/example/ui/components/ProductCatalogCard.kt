package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Product
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun ProductCatalogCard(
    product: Product,
    userRole: UserRole,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("product_card_${product.sku}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Product Hero Image & Packaging Size Badge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color(0xFFF1F5F9))
            ) {
                Image(
                    painter = painterResource(id = product.drawableRes),
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Packaging Tag
                Surface(
                    shape = RoundedCornerShape(topStart = 0.dp, bottomEnd = 12.dp),
                    color = QuantumNavy,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Text(
                        text = product.size,
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }

                // Quantum Cash Reward Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = QuantumEmerald,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "+₹${product.cashBackQuantumCash.toInt()} Cash",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = product.name,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
                Text(
                    text = product.subName,
                    fontSize = 12.sp,
                    color = TextSecondary,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(10.dp))

                // "Best Price Psychology" Matrix Box
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFF0FDF4),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${product.competitorBrand}: ",
                                    fontSize = 11.sp,
                                    color = TextMuted
                                )
                                Text(
                                    text = "₹${product.competitorPrice.toInt()}",
                                    fontSize = 11.sp,
                                    color = CompetitorRed,
                                    textDecoration = TextDecoration.LineThrough,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Quantum Price: ",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = QuantumNavy
                                )
                                val displayPrice = when (userRole) {
                                    UserRole.CUSTOMER -> product.b2cPrice
                                    UserRole.DEALER -> product.dealerPrice
                                    UserRole.SUPER_ADMIN -> product.b2bPrice
                                }
                                Text(
                                    text = "₹${displayPrice.toInt()}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = QuantumEmerald
                                )
                            }
                        }

                        // Savings Percent Badge
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SavingsBadgeGreen
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = "Save",
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "${product.savingsPercent}% OFF",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Dilution & Key Specs
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Dilution",
                        tint = QuantumMint,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Dilution: ${product.dilutionRatio}",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Add To Cart Button
                Button(
                    onClick = { onAddToCart(product) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("add_to_cart_${product.sku}"),
                    colors = ButtonDefaults.buttonColors(containerColor = QuantumNavy),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Add to Order",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
