package com.example.ecommerce.presentation.auth

import android.content.Context
import android.content.Intent
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
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
import android.provider.Settings
import com.example.ecommerce.domain.model.UserProfile
import com.example.ecommerce.domain.usecase.authusecase.GetCurrentUserIdUseCase
import com.example.ecommerce.domain.usecase.authusecase.LogoutUseCase
import com.example.ecommerce.domain.usecase.authusecase.ResetPasswordUseCase
import com.example.ecommerce.domain.usecase.settingusecase.UserProfileUseCase
import com.google.firebase.auth.FirebaseUser


@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val signupUseCase: SignupUseCase,
    private val googleSignInUseCase: GoogleSignInUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val logOutUseCase: LogoutUseCase,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val credentialManager: CredentialManager,
    private val request: GetCredentialRequest,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val userProfileUseCase: UserProfileUseCase
) : ViewModel() {

    private val _authState = MutableStateFlow<Result<String>>(Result.Idle)
    val authState = _authState.asStateFlow()

    private val _googleAuthState = MutableStateFlow<Result<FirebaseUser>>(Result.Idle)
    val googleAuthState = _googleAuthState.asStateFlow()
    private val _openAddGoogleAccountEvent = MutableStateFlow(false)
    val openAddGoogleAccountEvent = _openAddGoogleAccountEvent.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
                _authState.emit(Result.Loading)
                delay(500)
                val result = loginUseCase(email, password)
                _authState.value = result
                if (result is Result.Success) {
                    userPreferencesRepository.setLoggedIn(true)
                    userPreferencesRepository.setFirstTimeLogin(false)
                }
        }
    }

    fun signup(email: String, password: String, confirmPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
                _authState.emit(Result.Loading)
                delay(500)
                val result = signupUseCase(email, password, confirmPassword)
                _authState.value = result
                if (result is Result.Success) {
                    userPreferencesRepository.setLoggedIn(false)
                    userPreferencesRepository.setFirstTimeLogin(false)

                    val userProfile = UserProfile(
                        userId = getCurrentUserIdUseCase(),
                        emailAddress = email
                    )
                    userProfileUseCase.saveUserProfile(userProfile)
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
                    _googleAuthState.value = result
                    if (result is Result.Success) {
                        userPreferencesRepository.setLoggedIn(true)
                        userPreferencesRepository.setFirstTimeLogin(false)

                        val firebaseUser = result.data

                        val existingProfile = userProfileUseCase.getUserProfile(firebaseUser.uid)

                        if (existingProfile is Result.Success && existingProfile.data != null) {

                            // User profile exists - don't overwrite anything

                        } else {
                            // First time sign in - create new profile
                            val userProfile = UserProfile(
                                userId = firebaseUser.uid,
                                emailAddress = firebaseUser.email ?: "",
                                displayName = firebaseUser.displayName ?: "",
                                profilePicture = firebaseUser.photoUrl?.toString() ?: ""
                            )
                            userProfileUseCase.saveUserProfile(userProfile)
                        }
                    }
                }
            } catch (e: GetCredentialCancellationException) {
                // User canceled the dialog → do nothing
            } catch (e: NoCredentialException) {
                _openAddGoogleAccountEvent.value=true
                // No Google account available
            } catch (e: Exception) {
                // Unexpected error → only log or show a toast to avoid crash
                e.printStackTrace()
            }

        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.emit(Result.Loading)

            val result = logOutUseCase()

            if (result is Result.Success) {
                userPreferencesRepository.setLoggedIn(false)
            }

            _authState.value = result
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.emit(Result.Loading)
            delay(500)
            val result = resetPasswordUseCase(email)
            _authState.value = result
        }
    }


    fun resetAddGoogleAccountEvent() {
        _openAddGoogleAccountEvent.value = false
    }

    fun getAddGoogleAccountIntent(): Intent {
        return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
            putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
        }
    }

    fun resetAuthState() {
        _authState.value = Result.Idle
    }

}