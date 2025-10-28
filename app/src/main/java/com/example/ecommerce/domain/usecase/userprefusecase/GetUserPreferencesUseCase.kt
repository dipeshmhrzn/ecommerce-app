package com.example.ecommerce.domain.usecase.userprefusecase

import com.example.ecommerce.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserPreferencesUseCase @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) {

    fun isFirstTimeLogin(): Flow<Boolean> = userPreferencesRepository.isFirstTimeLogin

    fun isLoggedIn(): Flow<Boolean> = userPreferencesRepository.isLoggedIn
}