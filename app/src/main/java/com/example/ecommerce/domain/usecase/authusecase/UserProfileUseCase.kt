package com.example.ecommerce.domain.usecase.authusecase

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
}
