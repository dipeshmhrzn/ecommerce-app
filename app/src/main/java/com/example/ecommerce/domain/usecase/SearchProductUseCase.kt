package com.example.ecommerce.domain.usecase

import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.repository.ProductRepository
import com.example.ecommerce.domain.util.Result
import javax.inject.Inject

class SearchProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(query: String): Result<List<Product>> {
        return if (query.isBlank()){
            repository.getProducts()
        }else{
            repository.searchProducts(query)
        }
    }
}