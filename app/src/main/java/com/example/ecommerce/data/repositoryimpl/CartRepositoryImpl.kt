package com.example.ecommerce.data.repositoryimpl

import android.util.Log
import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.data.local.datastore.CartDataStore
import com.example.ecommerce.domain.model.CartItem
import com.example.ecommerce.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class CartRepositoryImpl(
    private val cartDataStore: CartDataStore
) : CartRepository {
    override fun getCartItem(): Flow<List<CartItem>> {
        return cartDataStore.cartItems
    }

    override suspend fun addItemToCart(product: Product, quantity: Int) {
        cartDataStore.addToCart(product, quantity)
        Log.d("CartRepositoryImpl", "Item added to cart: $product")
    }

    override suspend fun updateQuantity(productId: Int, newQuantity: Int) {
        cartDataStore.updateQuantity(productId, newQuantity)
    }

    override suspend fun getTotalCount(): Int {
        return cartDataStore.getCartItemCount()
    }

    override suspend fun removeSelectedItems(productIds: List<Int>){
        return cartDataStore.removeSelectedItemsFromCart(productIds)
    }
}