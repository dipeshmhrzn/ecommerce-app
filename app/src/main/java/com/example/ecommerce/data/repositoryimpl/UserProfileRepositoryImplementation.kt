package com.example.ecommerce.data.repositoryimpl

import android.util.Log
import com.example.ecommerce.domain.model.UserProfile
import com.example.ecommerce.domain.repository.UserProfileRepository
import com.example.ecommerce.domain.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.tasks.await

class UserProfileRepositoryImplementation(
    private val firebaseFirestore: FirebaseFirestore,
    private val supabaseClient: SupabaseClient

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
            val userId = userProfile.userId
                ?: return Result.Error("User ID is required for saving profile")

            firebaseFirestore
                .collection("users")
                .document(userId)
                .set(userProfile)
                .await()
            Result.Success("User profile saved successfully")
        } catch (e: Exception) {
            Result.Error("Error saving user profile: ${e.message}")
        }
    }

    override suspend fun uploadProfileImage(
        userId: String,
        bytes: ByteArray,
        mimeType: String
    ): Result<String> {
        val extension = when {
            mimeType.contains("png", ignoreCase = true) -> "png"
            mimeType.contains("webp", ignoreCase = true) -> "webp"
            else -> "jpg"
        }

        val fileName = "profile_${userId}_${System.currentTimeMillis()}.$extension"



        try {
            val bucket = supabaseClient.storage.from("profile-picture")

            bucket.upload(
                path = fileName,
                data = bytes
            ) {
                upsert = true        // this is a property on UploadOptionBuilder
                // you can also set contentType if you want, but it's optional.
                // contentType = mimeType
            }

            val publicUrl = bucket.publicUrl(fileName)
            Log.d("UserProfile", "saveUserProfile: $publicUrl")

            return Result.Success(publicUrl)

        } catch (e: Exception) {
            Log.d("UserProfile", "saveUserProfile: ${e.localizedMessage}")
            return Result.Error( "Failed to upload image")
        }
    }
}