package com.example.ecommerce.domain.usecase.wishlistusecase

import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllWishListItemsUseCase @Inject constructor(
    private val repository: WishlistRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getAllWishlistItems()
    }
}