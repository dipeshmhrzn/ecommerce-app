package com.example.ecommerce.presentation.wishlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.ecommerce.R
import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.ui.theme.Montserrat
import java.util.Locale

@Composable
fun WishlistCard(product: Product, onDeleteClick: () -> Unit, onItemClick: () -> Unit) {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .clickable {
                onItemClick()
            },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(context)
                    .data(product.thumbnail)
                    .crossfade(true)
                    .build(),
                modifier = Modifier
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5)),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )

                    }
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF5F5F5)),

                        ) {
                        Text(
                            text = "No image",
                            fontFamily = Montserrat,
                            fontSize = 12.sp
                        )
                    }

                }
            )
            Column {
                Text(
                    text = product.title,
                    fontSize = 22.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                )

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Rs.",
                        fontSize = 16.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFF83758)
                    )
                    Text(
                        text = String.format(Locale.US, "%.2f", product.price * 141),
                        fontSize = 20.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF83758)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Rs. ${
                            String.format(
                                Locale.US,
                                format = "%.2f",
                                (product.price * 141) + ((product.discountPercentage / 100) * (product.price * 141))
                            )
                        }", fontSize = 14.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                    Text(
                        text = "-${String.format(Locale.US, "%.2f", product.discountPercentage)}%",
                        fontSize = 14.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFF83758),
                        modifier = Modifier.background(color = Color(0xFFF83758).copy(alpha = .15f))
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating star",
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFFFFA726)
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = product.rating.toString() + "(${product.reviews.size})",
                        fontSize = 12.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            tint = Color(0xFF323232),
                            modifier = Modifier.size(20.dp)

                        )
                    }
                    IconButton(
                        onClick = { },
                        modifier = Modifier
                            .background(
                                color = Color(0xFFF83758),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .size(height = 35.dp, width = 50.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.addtocart),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }


                }
            }
        }
    }
}