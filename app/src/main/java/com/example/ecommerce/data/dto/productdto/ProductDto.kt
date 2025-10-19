package com.example.ecommerce.data.dto.productdto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val limit: Int,
    val products: List<Product>,
    val skip: Int,
    val total: Int
)