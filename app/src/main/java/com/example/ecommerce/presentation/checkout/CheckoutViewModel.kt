package com.example.ecommerce.presentation.checkout

import androidx.lifecycle.ViewModel
import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.model.CartItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class CheckoutViewModel @Inject constructor() : ViewModel() {

    private val _checkoutItems = MutableStateFlow<List<CartItem>>(emptyList())
    val checkoutItems = _checkoutItems.asStateFlow()

    fun checkoutFromProduct(product: Product) {
        _checkoutItems.value = listOf(
            CartItem(product = product, quantity = 1)
        )
    }

    fun checkoutFromCart(cartItems: List<CartItem>) {
        _checkoutItems.value = cartItems
    }

    fun clearCheckout() {
        _checkoutItems.value = emptyList()
    }
}

