package com.example.ecommerce.data.repositoryimpl

import com.example.ecommerce.data.local.UserPreferencesDataStore
import com.example.ecommerce.domain.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPreferencesRepositoryImpl(
    private val userPreferencesDataStore: UserPreferencesDataStore
): UserPreferencesRepository {

    override val isFirstTimeLogin : Flow<Boolean> = userPreferencesDataStore.isFirstTimeLogin

    override val isLoggedIn : Flow<Boolean> = userPreferencesDataStore.isLoggedIn

    override suspend fun setFirstTimeLogin(isFirstTime: Boolean){
        userPreferencesDataStore.setFirstTimeLogin(isFirstTime)
    }

    override suspend fun setLoggedIn(isLoggedIn: Boolean) {
        userPreferencesDataStore.setLoggedIn(isLoggedIn)
    }


}