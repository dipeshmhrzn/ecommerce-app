package com.example.ecommerce.presentation.cart

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.example.ecommerce.R
import com.example.ecommerce.navigation.Routes
import com.example.ecommerce.presentation.checkout.CheckoutViewModel
import com.example.ecommerce.ui.theme.Montserrat
import com.example.ecommerce.utils.sharedViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navHostController: NavHostController,
    viewModel: CartViewModel = hiltViewModel(),
    backStackEntry: NavBackStackEntry
) {

    val checkoutViewModel = backStackEntry.sharedViewModel<CheckoutViewModel>(navHostController)


    val context = LocalContext.current

    val state by viewModel.state.collectAsState()



    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        containerColor = Color(0xFFF9F9F9),
        topBar = {

            CenterAlignedTopAppBar(
                navigationIcon = {
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
                },
                title = {
                    Text(
                        text = "My Cart",
                        fontSize = 22.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                },
                actions = {
                    if (state.cartItems.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                viewModel.removeSelectedItems()
                            },
                            modifier = Modifier
                                .background(color = Color(0xFFF2F2F2), shape = CircleShape)
                                .size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.DeleteForever,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF9F9F9)
                ),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        },
        bottomBar = {
            if (state.cartItems.isNotEmpty()) {
                CartBottomBar(
                    totalItems = state.totalItems,
                    totalPrice = state.totalPrice,
                    isAllSelected = state.isAllSelected,
                    onAllSelectionToggle = {
                        viewModel.toggleAllSelection()
                    },
                    onCheckout = {
//                        val selectedItems = state.cartItems.filter {
//                            state.selectedItems.contains(it.product.id)
//                        }
//
//                        checkoutViewModel.checkoutFromCart(selectedItems)
                        val selectedItems = state.cartItems.filter {
                            state.selectedItems.contains(it.product.id)
                        }

                        if (selectedItems.isEmpty()) {
                            Toast.makeText(context, "Select at least one item", Toast.LENGTH_SHORT).show()
                            return@CartBottomBar
                        }

                        checkoutViewModel.checkoutFromCart(selectedItems)
                        navHostController.navigate(Routes.CheckoutScreen)
                    }
                )
            }
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> {
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

                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error ?: "Unknown error occurred !!"
                        )
                    }
                }

                state.cartItems.isEmpty() -> {

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Your cart is empty !!",
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

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.cartItems) { item ->
                            CartItemCard(
                                cartItem = item,
                                onQuantityIncrease = {
                                    viewModel.updateItemQuantity(
                                        productId = item.product.id,
                                        newQuantity = item.quantity + 1
                                    )
                                },
                                isSelected = state.selectedItems.contains(item.product.id),
                                onSelectionToggle = {
                                    viewModel.toggleSelection(item.product.id)
                                },
                                onQuantityDecrease = {
                                    viewModel.updateItemQuantity(
                                        productId = item.product.id,
                                        newQuantity = item.quantity - 1
                                    )
                                },
                                onItemClick = {
                                    navHostController.navigate(Routes.ProductDetailScreen(item.product.id))
                                }
                            )
                        }
                    }
                }

            }


        }
    }

}