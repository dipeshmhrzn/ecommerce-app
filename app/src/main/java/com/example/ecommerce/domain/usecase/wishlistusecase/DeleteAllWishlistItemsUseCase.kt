package com.example.ecommerce.domain.usecase.wishlistusecase

import com.example.ecommerce.domain.repository.WishlistRepository

class DeleteAllWishlistItemsUseCase(
    private val repository: WishlistRepository
) {

    suspend operator fun invoke() {
        repository.deleteAllWishlistItems()
    }

}