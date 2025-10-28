package com.example.ecommerce.domain.usecase.productusecase

import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.repository.ProductRepository
import com.example.ecommerce.domain.util.Result
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(query: String,showAllOnBlank: Boolean = true): Result<List<Product>> {
        return if (query.isBlank()) {
            Result.Success(emptyList())
        } else {
            repository.searchProducts(query)
        }
    }
}