package com.example.ecommerce.domain.usecase

import com.example.ecommerce.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow

class GetUserPreferencesUseCase(
    private val userPreferencesRepository: UserPreferencesRepository
) {

    fun isFirstTimeLogin(): Flow<Boolean> = userPreferencesRepository.isFirstTimeLogin

    fun isLoggedIn(): Flow<Boolean> = userPreferencesRepository.isLoggedIn
}