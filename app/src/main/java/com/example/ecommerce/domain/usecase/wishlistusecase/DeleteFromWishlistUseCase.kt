package com.example.ecommerce.domain.usecase.wishlistusecase

import com.example.ecommerce.domain.repository.WishlistRepository

class DeleteFromWishlistUseCase(
    private val repository: WishlistRepository
) {

    suspend operator fun invoke(id: Int) {
        return repository.deleteFromWishlist(id)
    }

}