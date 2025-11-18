package com.example.ecommerce.presentation.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.domain.repository.UserPreferencesRepository
import com.example.ecommerce.domain.usecase.authusecase.GoogleSignInUseCase
import com.example.ecommerce.domain.usecase.authusecase.LoginUseCase
import com.example.ecommerce.domain.usecase.authusecase.SignupUseCase
import com.example.ecommerce.domain.util.Result
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
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
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val credentialManager: CredentialManager,
    private val request: GetCredentialRequest,
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

    fun signInWithGoogle(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val credentialResponse = credentialManager.getCredential(context, request)
                if (credentialResponse.credential is CustomCredential && credentialResponse.credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                    val googleSignInToken = GoogleIdTokenCredential.createFrom(credentialResponse.credential.data)
                    val idToken = googleSignInToken.idToken
                    val result = googleSignInUseCase(idToken)
                    _authState.value = result
                    if (result is Result.Success) {
                        userPreferencesRepository.setLoggedIn(true)
                        userPreferencesRepository.setFirstTimeLogin(false)
                    }
                }
            } catch (e: androidx.credentials.exceptions.GetCredentialCancellationException) {
                // User canceled the dialog → do nothing
            } catch (e: androidx.credentials.exceptions.NoCredentialException) {
                // No Google account available
            } catch (e: Exception) {
                // Unexpected error → only log or show a toast to avoid crash
                e.printStackTrace()
            }

        }
    }

    fun resetAuthState() {
        _authState.value = Result.Idle
    }

}