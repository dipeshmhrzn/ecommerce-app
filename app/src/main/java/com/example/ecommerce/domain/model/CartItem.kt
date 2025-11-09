package com.example.ecommerce.domain.model

import com.example.ecommerce.data.dto.productdto.Product
import kotlinx.serialization.Serializable

@Serializable
data class CartItem (
    val product: Product,
    val quantity: Int
)