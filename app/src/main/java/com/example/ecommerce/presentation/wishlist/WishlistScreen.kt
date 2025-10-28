package com.example.ecommerce.presentation.wishlist

import android.widget.Space
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.ecommerce.R
import com.example.ecommerce.navigation.Routes
import com.example.ecommerce.ui.theme.Montserrat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    viewModel: WishListViewModel = hiltViewModel(),
    navHostController: NavHostController
) {

    val context = LocalContext.current

    val wishlistState by viewModel.state.collectAsState()

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        containerColor = Color(0xFFF9F9F9),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 6.dp, bottom = 6.dp)
                    .statusBarsPadding()
                    .background(color = Color(0xFFF9F9F9)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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

                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        viewModel.searchWishListItems(searchQuery)
                    },
                    placeholder = {
                        Text(
                            text = "Search Wishlist..",
                            fontFamily = Montserrat,
                            color = Color(0xFFBBBBBB),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Light
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = Color(0xFFBBBBBB),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .heightIn(min = 56.dp)
                        .clip(shape = RoundedCornerShape(16.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            when {
                wishlistState.isLoading -> {


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

                wishlistState.error != null -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = wishlistState.error ?: "Unknown error occurred !!"
                        )
                    }


                }

                wishlistState.filteredProducts.isEmpty() && wishlistState.allProducts.isEmpty() -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your wishlist is empty !!",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                navHostController.navigate(Routes.HomeScreen) {
                                    popUpTo(Routes.HomeScreen) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            },
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF83758)
                            )
                        ) {
                            Text(
                                text = "Browse Products", fontFamily = Montserrat,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )
                        }
                    }
                }

                wishlistState.filteredProducts.isEmpty() && searchQuery.isNotEmpty() -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No products found for \"$searchQuery\"",
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                    }

                }

                else -> {

                    LazyColumn {
                        stickyHeader {

                            if (wishlistState.filteredProducts.isNotEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp)
                                        .background(color = Color.White),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${wishlistState.filteredProducts.size} items",
                                        fontFamily = Montserrat,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(start = 10.dp)
                                    )

                                    Box(
                                        modifier = Modifier
                                            .padding(end = 10.dp)
                                            .background(
                                                color = Color(0xFFF2F2F2),
                                                shape = RoundedCornerShape(10.dp)
                                            )
                                            .padding(8.dp)
                                            .height(25.dp)
                                            .wrapContentWidth()
                                            .clickable(onClick = {
                                                viewModel.deleteAllFromWishlist()
                                            }),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Clear all",
                                            fontFamily = Montserrat,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 18.sp,
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        items(wishlistState.filteredProducts) { product ->
                            WishlistCard(
                                product,
                                onDeleteClick = {
                                    viewModel.deleteFromWishList(id = product.id)
                                },
                                onItemClick = {
                                    navHostController.navigate(Routes.ProductDetailScreen(product.id))
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}