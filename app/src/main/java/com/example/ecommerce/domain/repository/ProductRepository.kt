package com.example.ecommerce.domain.repository

import com.example.ecommerce.data.dto.productdto.ProductDto

interface ProductRepository {

    suspend fun getProducts(): ProductDto

}