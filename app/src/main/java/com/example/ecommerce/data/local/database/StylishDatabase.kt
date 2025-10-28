package com.example.ecommerce.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.data.local.converter.ProductConverters
import com.example.ecommerce.data.local.dao.WishlistDao

@Database(
    entities = [Product::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ProductConverters::class)
abstract class StylishDatabase: RoomDatabase(){
    abstract fun wishlistDao(): WishlistDao
}