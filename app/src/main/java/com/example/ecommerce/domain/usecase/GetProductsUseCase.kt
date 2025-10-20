package com.example.ecommerce.domain.usecase

import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.repository.ProductRepository
import com.example.ecommerce.domain.util.Result
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
){
    suspend operator fun invoke(): Result<List<Product>>{
        return productRepository.getProducts()
    }
}