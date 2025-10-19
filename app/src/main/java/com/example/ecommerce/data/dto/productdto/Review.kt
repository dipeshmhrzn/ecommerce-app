package com.example.ecommerce.data.dto.productdto

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val comment: String,
    val date: String,
    val rating: Int,
    val reviewerEmail: String,
    val reviewerName: String
)