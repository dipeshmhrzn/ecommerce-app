package com.example.ecommerce.presentation.userpreferences

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.domain.model.UserPreferencesState
import com.example.ecommerce.domain.usecase.GetUserPreferencesUseCase
import com.example.ecommerce.domain.usecase.SetUserPreferenceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class UserPreferencesViewModel(
    private val getUserPreferencesUseCase: GetUserPreferencesUseCase,
    private val setUserPreferencesUseCase: SetUserPreferenceUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UserPreferencesState())
    val state: StateFlow<UserPreferencesState> = _state.asStateFlow()

    init {
        observeUserPreferences()
    }

    private fun observeUserPreferences() {
        viewModelScope.launch {
            combine(
                getUserPreferencesUseCase.isFirstTimeLogin(),
                getUserPreferencesUseCase.isLoggedIn()
            ) { isFirstTimeLogin, isLoggedIn ->

                Log.d("UserPreferencesViewModel", "DataStore values changed - isFirstTime: $isFirstTimeLogin, isLoggedIn: $isLoggedIn,, isLoading: ${_state.value.isLoading}")

                UserPreferencesState(
                    isFirstTimeLogin = isFirstTimeLogin,
                    isLoggedIn = isLoggedIn,
                    isLoading = false
                )

            }.collect {newState ->
                Log.d("UserPreferencesViewModel", "Updating state - isFirstTime: ${newState.isFirstTimeLogin}, isLoggedIn: ${newState.isLoggedIn}, isLoading: ${newState.isLoading}")
                _state.value = newState
            }
        }
    }

    fun setFirstTimeLogin(isFirstTime: Boolean){
        viewModelScope.launch {
            try {
                setUserPreferencesUseCase.setFirstTimeLogin(isFirstTime)

                _state.value = _state.value.copy(isFirstTimeLogin = isFirstTime)
            }catch (e: Exception){
                Log.e("UserPreferencesViewModel", "Failed to set first-time login status", e)
            }
        }
    }

    fun setLoggedIn(isLoggedIn: Boolean){
        viewModelScope.launch {
            try {
                setUserPreferencesUseCase.setLoggedIn(isLoggedIn)
                _state.value = _state.value.copy(isLoggedIn = isLoggedIn)
            }catch (e: Exception){
                Log.e("UserPreferencesViewModel", "Failed to set logged in status", e)
            }
        }
    }

}