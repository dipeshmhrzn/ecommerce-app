package com.example.ecommerce.domain.usecase.cartusecase

import com.example.ecommerce.domain.model.CartItem
import com.example.ecommerce.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val repository: CartRepository
) {
    operator fun invoke(): Flow<List<CartItem>> {
        return repository.getCartItem()
    }
}