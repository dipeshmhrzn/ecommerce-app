package com.example.ecommerce.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerce.R
import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.ui.theme.Montserrat
import java.util.Locale

@Composable
fun ProductDetailCard(
    product: Product,
    isItemInWishlist : Boolean,
    onWishlistClick: () -> Unit
) {

    var isDescriptionExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Rs.",
                    fontSize = 18.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF83758)
                )
                Text(
                    text = String.format(Locale.US, "%.2f", product.price * 141),
                    fontSize = 22.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF83758)
                )
                Text(
                    text = "Rs. ${
                        String.format(
                            Locale.US,
                            format = "%.2f",
                            (product.price * 141) + ((product.discountPercentage / 100) * (product.price * 141))
                        )
                    }",
                    fontSize = 16.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough
                )
                Text(
                    text = "-${String.format(Locale.US, "%.2f", product.discountPercentage)}%",
                    fontSize = 16.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFF83758),
                    modifier = Modifier.background(color = Color(0xFFF83758).copy(alpha = .15f))
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = product.title,
                fontSize = 24.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.description,
                fontSize = 18.sp,
                fontFamily = Montserrat,
                textAlign = TextAlign.Justify,
                maxLines = if (isDescriptionExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { isDescriptionExpanded = !isDescriptionExpanded }
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Rating star",
                    modifier = Modifier.size(22.dp),
                    tint = Color(0xFFFFA726)
                )

                Text(
                    text = "${product.rating} (${product.reviews.size})",
                    fontSize = 16.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = " | ${product.stock} in Stock",
                    fontSize = 16.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium,
                )



                Spacer(modifier = Modifier.weight(1f))

                Image(
                    imageVector = if (isItemInWishlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder ,
                    contentDescription = "Wishlist",
                    colorFilter = if (isItemInWishlist) ColorFilter.tint(Color(0xFFF83758)) else null,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            onClick = onWishlistClick
                        )
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            onClick = {}
                        )
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFFF9F9F9), shape = RoundedCornerShape(10.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.returnpolicy),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = product.returnPolicy,
                            fontSize = 16.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.shippingtruck),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = product.shippingInformation,
                            fontSize = 16.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.warranty),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = product.warrantyInformation,
                            fontSize = 16.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
        }
    }

}