package com.example.ecommerce.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ecommerce.data.dto.productdto.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {

    @Query("SELECT * FROM wishlist_table")
    fun getAllWishlistItems(): Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlistItem(product: Product)

    @Query("DELETE FROM wishlist_table WHERE id = :productId")
    suspend fun deleteWishlistItem(productId: Int)

    @Query("DELETE FROM wishlist_table")
    suspend fun deleteAllWishlistItems()

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_table WHERE id = :productId)")
    suspend fun isItemInWishlist(productId: Int): Boolean


}