package com.example.ecommerce.domain.repository

import com.example.ecommerce.domain.util.Result

interface AuthRepository {

    suspend fun login(email:String, password:String): Result<String>
    suspend fun signup(email: String, password: String):Result<String>
    suspend fun signInWithGoogle(idToken: String): Result<String>
    suspend fun resetPassword(email: String): Result<String>
    suspend fun logout(): Result<String>
}