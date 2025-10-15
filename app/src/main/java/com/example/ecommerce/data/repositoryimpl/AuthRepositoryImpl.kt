package com.example.ecommerce.data.repositoryimpl

import com.example.ecommerce.domain.repository.AuthRepository
import com.example.ecommerce.domain.util.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Result.Success("Login Successful")
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Error occurred")
        }
    }

    override suspend fun signup(email:String, password: String):Result<String>{
        return try {
            firebaseAuth.createUserWithEmailAndPassword(email,password).await()
            Result.Success("Sign up successful")
        }catch (e: Exception){
            Result.Error(e.localizedMessage ?: "Error occurred")
        }
    }

}