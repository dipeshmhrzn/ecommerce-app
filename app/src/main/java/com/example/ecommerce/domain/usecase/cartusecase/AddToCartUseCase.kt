package com.example.ecommerce.domain.usecase.cartusecase

import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val repository: CartRepository
){
    suspend operator fun invoke(product: Product,quantity: Int){
        return repository.addItemToCart(product,quantity)
    }
}