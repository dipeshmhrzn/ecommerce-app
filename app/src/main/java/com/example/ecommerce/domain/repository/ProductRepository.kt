package com.example.ecommerce.domain.repository

import com.example.ecommerce.data.dto.productdto.Product
import com.example.ecommerce.domain.util.Result

interface ProductRepository {

    suspend fun getProducts(): Result<List<Product>>

    suspend fun searchProducts(query: String): Result<List<Product>>

}