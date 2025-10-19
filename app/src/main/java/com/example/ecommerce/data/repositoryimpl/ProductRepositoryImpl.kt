package com.example.ecommerce.data.repositoryimpl

import com.example.ecommerce.data.dto.productdto.ProductDto
import com.example.ecommerce.data.remote.ProductApiServices
import com.example.ecommerce.domain.repository.ProductRepository
import com.example.ecommerce.domain.util.Result

class ProductRepositoryImpl(
    private val apiServices: ProductApiServices
) : ProductRepository {

    override suspend fun getProducts(): ProductDto {
        return try {
            apiServices.getProducts()
        } catch (e: Exception) {
            throw Exception("Failed to fetch data : ${e.localizedMessage}" )
        }
    }


}