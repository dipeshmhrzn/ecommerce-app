package com.example.ecommerce.domain.usecase.wishlistusecase

import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.repository.WishlistRepository
import javax.inject.Inject

class InsertIntoWishlistUseCase @Inject constructor(
    private val repository: WishlistRepository
) {

    suspend operator fun invoke(product: Product) {
        return repository.insertIntoWishlist(product)
    }
}