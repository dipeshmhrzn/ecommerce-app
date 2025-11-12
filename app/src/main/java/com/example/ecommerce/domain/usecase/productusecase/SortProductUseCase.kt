package com.example.ecommerce.domain.usecase.productusecase

import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.repository.ProductRepository
import com.example.ecommerce.domain.util.Result
import javax.inject.Inject

class SortProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(order: String): Result<List<Product>> {
        return repository.sortProduct(order)
    }
}