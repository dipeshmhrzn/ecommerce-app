package com.example.ecommerce.data.repositoryimpl

import com.example.ecommerce.domain.repository.AuthRepository
import com.example.ecommerce.domain.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
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

    override suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser> {
        return try {
            val credentials = GoogleAuthProvider.getCredential(idToken,null)
            val user = firebaseAuth.signInWithCredential(credentials).await().user
            if (user != null) {
                Result.Success(user)  // Return the FirebaseUser object
            } else {
                Result.Error("Google sign-in failed: No user data available")
            }
        }catch (e: Exception){
            Result.Error(e.localizedMessage ?: "Error occurred")
        }
    }

    override suspend fun resetPassword(email: String): Result<String> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.Success("Password reset email sent")
        }catch (e: Exception){
            Result.Error(e.localizedMessage ?: "Error occurred")
        }
    }

    override suspend fun logout(): Result<String> {
        return try {
            firebaseAuth.signOut() 
            Result.Success("Logout successful")
        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Logout failed")
        }
    }

    override fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }

}