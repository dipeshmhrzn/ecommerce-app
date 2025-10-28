package com.example.ecommerce.data.repositoryimpl

import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.data.local.dao.WishlistDao
import com.example.ecommerce.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow

class WishlistRepositoryImpl(
    private val wishlistDao: WishlistDao
) : WishlistRepository {

    override fun getAllWishlistItems(): Flow<List<Product>> {
        return wishlistDao.getAllWishlistItems()
    }

    override suspend fun insertIntoWishlist(product: Product) {
        return wishlistDao.insertWishlistItem(product)
    }

    override suspend fun deleteFromWishlist(id: Int) {
        return wishlistDao.deleteWishlistItem(id)
    }

    override suspend fun deleteAllWishlistItems() {
        return wishlistDao.deleteAllWishlistItems()
    }

    override suspend fun isItemInWishlist(id: Int): Boolean {
        return wishlistDao.isItemInWishlist(id)
    }

}