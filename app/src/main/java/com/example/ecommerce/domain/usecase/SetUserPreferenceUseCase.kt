package com.example.ecommerce.domain.usecase

import com.example.ecommerce.domain.repository.UserPreferencesRepository

class SetUserPreferenceUseCase(
    private val userPreferenceRepository: UserPreferencesRepository
) {
    suspend fun setFirstTimeLogin(isFirstTime: Boolean){
        userPreferenceRepository.setFirstTimeLogin(isFirstTime)
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean){
        userPreferenceRepository.setLoggedIn(isLoggedIn)
    }

}