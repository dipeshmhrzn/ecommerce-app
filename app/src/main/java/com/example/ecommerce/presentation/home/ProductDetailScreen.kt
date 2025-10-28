package com.example.ecommerce.presentation.home

import android.widget.Toast
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import com.example.ecommerce.R
import com.example.ecommerce.domain.util.Result
import com.example.ecommerce.navigation.Routes
import com.example.ecommerce.presentation.components.ProductCard
import com.example.ecommerce.presentation.components.ProductDetailCard
import com.example.ecommerce.presentation.components.ReviewCard
import com.example.ecommerce.presentation.components.StarRatings
import com.example.ecommerce.presentation.wishlist.WishListViewModel
import com.example.ecommerce.ui.theme.Montserrat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    id: Int,
    viewModel: ProductViewModel = hiltViewModel(),
    wishlistViewModel: WishListViewModel = hiltViewModel(),
    navHostController: NavHostController
) {

    val productState by viewModel.productState.collectAsState()

    var isItemInWishlist by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val product = when (val state = productState) {
        is Result.Success -> state.data.find { it.id == id }
        else -> null
    }

    val similarProducsts = when (val state = productState) {
        is Result.Success -> {
            val currentId = state.data.find { it.id == id }
            currentId?.let { product ->
                state.data.filter { it.category == product.category && it.id != product.id }
            } ?: emptyList()
        }

        else -> emptyList()
    }


    val images = product?.images ?: emptyList()
    val pagerState = rememberPagerState(pageCount = { images.size })

    LaunchedEffect(id) {
        isItemInWishlist = wishlistViewModel.isItemInWishlist(id)
    }

    LaunchedEffect(Unit) {
        wishlistViewModel.uiEvent.collect { message->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                navHostController.navigate(Routes.HomeScreen) {
                                    popUpTo(Routes.HomeScreen) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            },
                            modifier = Modifier
                                .background(color = Color(0xFFF2F2F2), shape = CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.arrowback),
                                contentDescription = null,
                                tint = Color(0xFF323232),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(45.dp)
                                .weight(1f)
                                .padding(start = 8.dp, end = 8.dp)
                                .background(Color.White, shape = RoundedCornerShape(16.dp))
                                .clickable(
                                    onClick = {
                                        navHostController.navigate(Routes.SearchScreen)
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                Icon(
                                    painter = painterResource(R.drawable.search),
                                    contentDescription = null,
                                    tint = Color(0xFFBBBBBB),
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Search in Stylish",
                                    fontFamily = Montserrat,
                                    color = Color(0xFFBBBBBB),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Light
                                )

                            }
                        }

                        IconButton(
                            onClick = { },
                            modifier = Modifier
                                .background(color = Color(0xFFF2F2F2), shape = CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.cart),
                                contentDescription = null,
                                tint = Color(0xFF323232),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                    }
                },
                modifier = Modifier.padding(end = 16.dp),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF9F9F9)
                )
            )
        }, bottomBar = {
            BottomAppBar(
                containerColor = Color.White
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { },
                        modifier = Modifier
                            .weight(1f)
                            .height(68.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray.copy(alpha = .5f)
                        )
                    ) {
                        Text(
                            text = "Add to Cart",
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = { },
                        modifier = Modifier
                            .weight(1f)
                            .height(68.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF83758)
                        )
                    ) {
                        if (product != null) {
                            Text(
                                text = "Buy now at \nRs. ${
                                    String.format(
                                        Locale.US,
                                        "%.2f",
                                        product.price * 141
                                    )
                                }",
                                fontSize = 18.sp,
                                color = Color.White,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )

                            }
                        }
                    }
                }
            }

        },
        containerColor = Color(0xFFF9F9F9)
    ) { paddingValues ->
        when {
            productState is Result.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )

                }
            }

            product == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No products found",
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()

                    ) {
                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) { page ->
                            SubcomposeAsyncImage(
                                model = images[page],
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFF5F5F5)),
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
                                        contentAlignment = Alignment.Center
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
                        if (images.isNotEmpty()) {
                            Text(
                                text = "${pagerState.currentPage + 1} / ${images.size}",
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(16.dp)
                                    .background(
                                        Color.Black.copy(alpha = 0.6f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    ProductDetailCard(
                        product = product,
                        isItemInWishlist= isItemInWishlist,
                        onWishlistClick = {
                            if (isItemInWishlist) {
                                wishlistViewModel.deleteFromWishList(product.id)
                                isItemInWishlist = false
                            } else {
                                wishlistViewModel.addToWishlist(product)
                                isItemInWishlist = true
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White)
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Rating & reviews",
                                fontSize = 20.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Text(
                                text = "(${product.reviews.size})",
                                fontSize = 18.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Medium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "(${product.rating})",
                                fontSize = 18.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Gray
                            )
                            StarRatings(product.rating)

                        }
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
                            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
                        )
                        product.reviews.forEach { item ->
                            ReviewCard(review = item)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Similar Products",
                            fontSize = 20.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(6.dp)
                        )
                        if (similarProducsts.isEmpty()) {
                            Text(
                                text = "No similar products",
                                fontSize = 20.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Medium, modifier = Modifier
                                    .padding(bottom = 16.dp)
                            )
                        } else {
                            val chunkedSimilarProduct = similarProducsts.chunked(2)

                            chunkedSimilarProduct.forEach { rowItems ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp)
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    rowItems.forEach { item ->
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .background(Color.White, RoundedCornerShape(10.dp))
                                        ) {
                                            ProductCard(
                                                item = item,
                                                onClick = {
                                                    navHostController.navigate(
                                                        Routes.ProductDetailScreen(
                                                            id = item.id
                                                        )
                                                    )
                                                })
                                        }
                                    }
                                    // Fill empty space if only 1 item in the last row
                                    if (rowItems.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
    }

}

