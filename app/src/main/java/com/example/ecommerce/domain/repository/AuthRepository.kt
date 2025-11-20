package com.example.ecommerce.domain.repository

import com.example.ecommerce.domain.util.Result
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun login(email:String, password:String): Result<String>
    suspend fun signup(email: String, password: String):Result<String>
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser>
    suspend fun resetPassword(email: String): Result<String>
    suspend fun logout(): Result<String>
    fun getCurrentUserId(): String?
}