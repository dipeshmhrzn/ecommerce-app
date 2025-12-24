package com.example.ecommerce.presentation.checkout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.util.getColumnIndex
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.ecommerce.R
import com.example.ecommerce.domain.model.CartItem
import com.example.ecommerce.ui.theme.Montserrat
import kotlin.math.roundToInt

@Composable
fun CheckoutCard(
    cartItem: CartItem
) {
    val product = cartItem.product
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = product.brand.toString(),
                fontSize = 16.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF5F5F5))
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(product.thumbnail)
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

                Spacer(modifier = Modifier.width(6.dp))

                Column {
                    Text(
                        text = product.title,
                        fontSize = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,

                        )
                    Text(
                        text = product.description,
                        fontSize = 14.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontFamily = Montserrat
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Row {
                        Text(
                            text = "Rs. ${product.price.roundToInt()*141}",
                            fontSize = 18.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Qty : ${cartItem.quantity}",
                            fontSize = 16.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(10.dp),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp)
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
        }
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp)
                .align(Alignment.BottomCenter)
                .offset(y = 24.dp)
        )
    }
}