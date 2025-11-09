package com.example.ecommerce.domain.repository

import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {

    fun getCartItem(): Flow<List<CartItem>>

    suspend fun addItemToCart(product: Product, quantity: Int = 1)

    suspend fun updateQuantity(productId: Int, newQuantity: Int)

    suspend fun getTotalCount(): Int

    suspend fun removeSelectedItems(productIds: List<Int>)


}