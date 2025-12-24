package com.example.ecommerce.presentation.checkout

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.example.ecommerce.BuildConfig
import com.example.ecommerce.R
import com.example.ecommerce.data.remote.StripeService
import com.example.ecommerce.navigation.Routes
import com.example.ecommerce.presentation.cart.CartViewModel
import com.example.ecommerce.ui.theme.Montserrat
import com.example.ecommerce.utils.sharedViewModel
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOutScreen(
    navHostController: NavHostController,
    backStackEntry: NavBackStackEntry,
    cartViewModel: CartViewModel = hiltViewModel()
) {
    val checkoutViewModel = backStackEntry.sharedViewModel<CheckoutViewModel>(navHostController)

    val items by checkoutViewModel.checkoutItems.collectAsState()

    val context = LocalContext.current

    val totalItems = items.sumOf { it.quantity }
    val totalPrice = items.sumOf { it.product.price.roundToInt() * it.quantity } * 141

    val scope = rememberCoroutineScope()
    val stripeService = remember { StripeService(BuildConfig.SECRET_KEY) }
    val paymentSheet = rememberPaymentSheet { result ->
        when (result) {
            is PaymentSheetResult.Canceled -> {
                Toast.makeText(context, "Payment canceled", Toast.LENGTH_SHORT).show()
            }

            is PaymentSheetResult.Failed -> {
                Toast.makeText(
                    context,
                    "Payment failed : ${result.error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            is PaymentSheetResult.Completed -> {
                Toast.makeText(context, "Order has been placed successfully", Toast.LENGTH_SHORT)
                    .show()
                checkoutViewModel.clearCheckout()


                // Remove purchased items from cart
                val purchasedIds = items.map { it.product.id }
                cartViewModel.removePurchasedItems(purchasedIds)

                // Navigate to Home screen
                navHostController.navigate(Routes.HomeScreen) {
                    popUpTo(Routes.HomeScreen) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }


    Scaffold(
        containerColor = Color(0xFFF9F9F9),
        topBar = {

            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navHostController.popBackStack()
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
                        text = "Checkout",
                        fontSize = 22.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF9F9F9)
                ),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 16.dp, end = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        Column {
                            Text(
                                text = "Total ($totalItems items)",
                                fontSize = 14.sp,
                                fontFamily = Montserrat,
                                color = Color.Gray
                            )
                            Text(
                                text = "Rs. $totalPrice",
                                fontSize = 22.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Button(
                            onClick = {
                                scope.launch {
                                    try {
                                        val result = stripeService.createPaymentIntent(totalPrice)
                                        if (result.isSuccess) {
                                            val clientSecret = result.getOrNull()
                                            if (clientSecret != null) {
                                                presentPaymentSheet(paymentSheet, clientSecret)
                                            } else {
                                                Toast.makeText(
                                                    context,
                                                    "No secret key",
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                            }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Payment failed: ${result.exceptionOrNull()?.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "Payment failed: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF83758)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(56.dp)
                                .width(175.dp),
                        ) {
                            Text(
                                text = "Proceed to Pay",
                                fontFamily = Montserrat,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {

            items.forEach {
                CheckoutCard(cartItem = it)
            }

        }

    }
}


fun presentPaymentSheet(paymentSheet: PaymentSheet, clientSecret: String) {
    val configuration = PaymentSheet.Configuration(
        merchantDisplayName = "Stylish",
        allowsDelayedPaymentMethods = false
    )

    paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret = clientSecret,
        configuration = configuration
    )
}