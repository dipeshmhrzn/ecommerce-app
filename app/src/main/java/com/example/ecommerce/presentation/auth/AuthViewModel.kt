package com.example.ecommerce.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.domain.repository.UserPreferencesRepository
import com.example.ecommerce.domain.usecase.LoginUseCase
import com.example.ecommerce.domain.usecase.SignupUseCase
import com.example.ecommerce.domain.util.Result
import com.example.ecommerce.presentation.userpreferences.UserPreferencesViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignupUseCase,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<Result<String>>(Result.Idle)
    val authState = _authState.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _authState.emit(Result.Loading)
                delay(500)
                val result = loginUseCase(email, password)
                _authState.value = result
                if (result is Result.Success) {
                    userPreferencesRepository.setLoggedIn(true)
                    userPreferencesRepository.setFirstTimeLogin(false)
                }
            } catch (e: Exception) {
                _authState.value = Result.Error(e.localizedMessage ?: "Login Failed")
            }
        }
    }

    fun signup(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                println("STATE: Loading")
                _authState.emit(Result.Loading)
                delay(500)
                val result = signupUseCase(email, password, confirmPassword)
                _authState.value = result
                if (result is Result.Success) {
                    userPreferencesRepository.setLoggedIn(false)
                    userPreferencesRepository.setFirstTimeLogin(false)
                }
            } catch (e: Exception) {
                _authState.value = Result.Error(e.localizedMessage ?: "Signup Failed")
            }
        }
    }

    fun resetAuthState() {
        _authState.value = Result.Idle
    }

}