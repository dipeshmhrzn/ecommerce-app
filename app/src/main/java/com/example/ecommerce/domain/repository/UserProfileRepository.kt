package com.example.ecommerce.domain.repository

import com.example.ecommerce.domain.model.UserProfile
import com.example.ecommerce.domain.util.Result

interface UserProfileRepository {
    suspend fun saveUserProfile(userProfile: UserProfile): Result<String>
    suspend fun getUserProfile(userId: String): Result<UserProfile>

    suspend fun encodeToBase64(bytes: ByteArray): String
}