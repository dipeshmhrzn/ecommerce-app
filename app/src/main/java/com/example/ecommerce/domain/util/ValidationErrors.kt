package com.example.ecommerce.domain.util

sealed class ValidationErrors {

    data class EmailError(val message: String) : ValidationErrors()
    data class PasswordError(val message: String) : ValidationErrors()

    data class ConfirmPasswordError(val message: String) : ValidationErrors()

}