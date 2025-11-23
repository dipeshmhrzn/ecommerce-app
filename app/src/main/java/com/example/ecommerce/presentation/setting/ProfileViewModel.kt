package com.example.ecommerce.presentation.setting

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.domain.model.UserProfile
import com.example.ecommerce.domain.usecase.authusecase.GetCurrentUserIdUseCase
import com.example.ecommerce.domain.usecase.settingusecase.EncodeProfileImageUseCase
import com.example.ecommerce.domain.usecase.settingusecase.UserProfileUseCase
import com.example.ecommerce.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    private val encodeProfileImageUseCase: EncodeProfileImageUseCase,
    @ApplicationContext private val appContext: Context,
) : ViewModel() {

    private val _userProfile = MutableStateFlow<Result<UserProfile?>>(Result.Idle)
    val userProfile = _userProfile.asStateFlow()

    fun getUserProfile() {
        viewModelScope.launch {
            _userProfile.value = Result.Loading
            getCurrentUserIdUseCase()?.let { userId ->
                val result = userProfileUseCase.getUserProfile(userId)
                _userProfile.value = result
            }
        }
    }

    fun saveUserProfile(
        userProfile: UserProfile,
        selectedImageUri: Uri?
    ) {
        viewModelScope.launch {
            _userProfile.value = Result.Loading

            val userId = getCurrentUserIdUseCase()

            if (userId == null) {
                _userProfile.value = Result.Error("User not logged in")
                return@launch
            }

            var base64Image = userProfile.profilePicture ?: ""

            if (selectedImageUri != null) {
                val bytes = try {
                    appContext.contentResolver
                        .openInputStream(selectedImageUri)
                        ?.use { it.readBytes() }
                        ?: run {
                            _userProfile.value = Result.Error("Failed to read selected image")
                            return@launch
                        }
                } catch (e: Exception) {
                    _userProfile.value = Result.Error("Failed to read selected image")
                    return@launch
                }
                base64Image = encodeProfileImageUseCase(bytes)
            }

            val profileToSave = userProfile.copy(
                userId = userId,
                profilePicture = base64Image
            )

            val result = userProfileUseCase.saveUserProfile(profileToSave)

            _userProfile.value = when (result) {
                is Result.Success -> Result.Success(profileToSave)
                is Result.Error -> Result.Error(result.message)
                is Result.Loading -> Result.Loading
                is Result.Idle -> Result.Idle
            }
        }
    }
}
