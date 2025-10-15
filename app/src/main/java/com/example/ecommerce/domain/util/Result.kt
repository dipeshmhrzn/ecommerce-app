package com.example.ecommerce.domain.util

sealed class Result<out t>{

    data object Idle : Result<Nothing>()
    data object Loading: Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: Any) : Result<Nothing>()

}