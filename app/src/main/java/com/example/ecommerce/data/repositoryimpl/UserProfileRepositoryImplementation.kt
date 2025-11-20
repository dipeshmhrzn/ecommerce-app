package com.example.ecommerce.data.repositoryimpl

import com.example.ecommerce.domain.model.UserProfile
import com.example.ecommerce.domain.repository.UserProfileRepository
import com.example.ecommerce.domain.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserProfileRepositoryImplementation(
    private val firebaseFirestore: FirebaseFirestore
): UserProfileRepository{

    override suspend fun getUserProfile(userId: String): Result<UserProfile> {
        return try {
            val userRef = firebaseFirestore.collection("users").document(userId)
            val document = userRef.get().await()
            if (document.exists()) {
                val userProfile = document.toObject(UserProfile::class.java) ?: UserProfile()
                Result.Success(userProfile)
            } else {
                Result.Error("User profile not found")
            }
        } catch (e: Exception) {
            Result.Error("Error fetching user profile: ${e.message}")
        }
    }

    override suspend fun saveUserProfile(userProfile: UserProfile): Result<String> {
        return try {
            val userRef = firebaseFirestore.collection("users").document(userProfile.userId!!)
            userRef.set(userProfile).await()
            Result.Success("User profile saved successfully")
        } catch (e: Exception) {
            Result.Error("Error saving user profile: ${e.message}")
        }

    }
}