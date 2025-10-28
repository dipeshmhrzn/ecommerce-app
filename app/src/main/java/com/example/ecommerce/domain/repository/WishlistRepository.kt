package com.example.ecommerce.domain.repository

import com.example.ecommerce.data.dto.productdto.Product
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {

    fun getAllWishlistItems(): Flow<List<Product>>

    suspend fun insertIntoWishlist(product: Product)

    suspend fun deleteFromWishlist(id: Int)

    suspend fun deleteAllWishlistItems()

    suspend fun isItemInWishlist(id: Int): Boolean


}