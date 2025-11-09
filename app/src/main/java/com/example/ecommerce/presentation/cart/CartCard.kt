package com.example.ecommerce.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.ecommerce.domain.model.CartItem
import com.example.ecommerce.ui.theme.Montserrat
import kotlin.math.roundToInt

@Composable
fun CartItemCard(
    isSelected: Boolean,
    onSelectionToggle: () -> Unit,
    cartItem: CartItem,
    onQuantityIncrease: () -> Unit,
    onQuantityDecrease: () -> Unit,
    onItemClick: () -> Unit
) {

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
                .padding(top = 10.dp, bottom = 10.dp)
        ) {

            Checkbox(
                checked = isSelected,
                onCheckedChange = { onSelectionToggle() },
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF5F5F5))
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(context)
                        .data(cartItem.product.thumbnail)
                        .crossfade(true)
                        .build(),
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
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
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                Text(
                    text = cartItem.product.title,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.height(8.dp))
                cartItem.product.brand?.let {
                    Text(
                        text = it,
                        fontSize = 16.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Medium,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
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
                                text = "${(cartItem.product.price).roundToInt() * 141}",
                                fontSize = 20.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF83758)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Rs. ${((cartItem.product.price * 141) + ((cartItem.product.discountPercentage / 100) * (cartItem.product.price * 141))).roundToInt()}",
                            fontSize = 16.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )

                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = onQuantityDecrease,
                            enabled = cartItem.quantity > 1,
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = if (cartItem.quantity > 1) Color(0xFFF2F2F2) else Color(
                                            0xFFF2F2F2
                                        ).copy(.3f)
                                    )
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Remove,
                                    contentDescription = "Increase",
                                    tint = if (cartItem.quantity > 1) Color.Black else Color.Black.copy(
                                        .2f
                                    ),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Text(
                            text = cartItem.quantity.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                        )

                        IconButton(
                            onClick = onQuantityIncrease
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(color = Color(0xFFF2F2F2))
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Increase",
                                    tint = Color.Black,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                }

            }


        }
    }
}

