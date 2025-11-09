package com.example.ecommerce.domain.usecase.cartusecase

import com.example.ecommerce.domain.repository.CartRepository
import javax.inject.Inject

class RemoveSelectedCartItemsUseCase @Inject constructor(
    private val repository: CartRepository
) {
    suspend operator fun invoke(productIds: List<Int>) {
        repository.removeSelectedItems(productIds)
    }
}