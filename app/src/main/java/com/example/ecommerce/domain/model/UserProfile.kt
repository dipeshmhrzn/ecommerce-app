package com.example.ecommerce.domain.model

data class UserProfile(
    val userId: String? = null,
    val emailAddress: String = "",
    val displayName: String = "",
    val profilePicture: String? = null,
    val address: String = "",
    val city: String = "",
    val country: String = ""
)
