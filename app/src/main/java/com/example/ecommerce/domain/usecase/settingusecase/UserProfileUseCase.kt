package com.example.ecommerce.domain.usecase.settingusecase

import com.example.ecommerce.domain.model.UserProfile
import com.example.ecommerce.domain.repository.UserProfileRepository
import com.example.ecommerce.domain.util.Result
import javax.inject.Inject

class UserProfileUseCase @Inject constructor(
    private val userProfileRepository: UserProfileRepository
) {

    suspend fun saveUserProfile(userProfile: UserProfile): Result<String> {
        return userProfileRepository.saveUserProfile(userProfile)
    }

    suspend fun getUserProfile(userId: String): Result<UserProfile?> {
        return userProfileRepository.getUserProfile(userId)
    }

    suspend fun uploadProfileImage(
        userId: String,
        bytes: ByteArray,
        mimeType: String
    ): Result<String> {
        return userProfileRepository.uploadProfileImage(userId, bytes, mimeType)
    }
}