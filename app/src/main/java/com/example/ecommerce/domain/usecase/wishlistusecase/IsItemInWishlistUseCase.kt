package com.example.ecommerce.domain.usecase.wishlistusecase

import com.example.ecommerce.domain.repository.WishlistRepository

class IsItemInWishlistUseCase(
    private val repository: WishlistRepository
) {

    suspend operator fun invoke(id: Int): Boolean {
        return repository.isItemInWishlist(id)
    }
}