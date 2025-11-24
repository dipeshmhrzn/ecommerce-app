package com.example.ecommerce.presentation.setting

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.domain.model.UserProfile
import com.example.ecommerce.domain.usecase.authusecase.GetCurrentUserIdUseCase
import com.example.ecommerce.domain.usecase.settingusecase.UserProfileUseCase
import com.example.ecommerce.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase,
    @ApplicationContext private val appContext: Context,
) : ViewModel() {

    private val _userProfile = MutableStateFlow<Result<UserProfile?>>(Result.Idle)
    val userProfile = _userProfile.asStateFlow()

    fun getUserProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _userProfile.value = Result.Loading
            val userId = getCurrentUserIdUseCase()
            if (userId == null) {
                _userProfile.value = Result.Error("User not logged in")
                return@launch
            }

            val result = userProfileUseCase.getUserProfile(userId)
            _userProfile.value = result
        }
    }

    fun saveUserProfile(
        userProfile: UserProfile,
        selectedImageUri: Uri?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _userProfile.value = Result.Loading

            val userId = getCurrentUserIdUseCase()
            if (userId == null) {
                _userProfile.value = Result.Error("User not logged in")
                return@launch
            }

            var finalProfilePicture = userProfile.profilePicture

            // 1) Upload new image if user picked one
            if (selectedImageUri != null) {
                try {
                    val resolver = appContext.contentResolver

                    val mimeType = resolver.getType(selectedImageUri) ?: "image/jpeg"

                    val bytes = withContext(Dispatchers.IO) {
                        resolver.openInputStream(selectedImageUri)?.use { it.readBytes() }
                    }

                    if (bytes == null) {
                        _userProfile.value = Result.Error("Unable to read selected image")
                        return@launch
                    }

                    when (val uploadResult =
                        userProfileUseCase.uploadProfileImage(userId, bytes, mimeType)) {
                        is Result.Success -> {
                            finalProfilePicture = uploadResult.data
                        }

                        is Result.Error -> {
                            _userProfile.value =
                                Result.Error(uploadResult.message ?: "Failed to upload image")
                            return@launch
                        }

                        else -> {}
                    }
                } catch (e: Exception) {
                    _userProfile.value =
                        Result.Error("Error processing selected image: ${e.message}")
                    return@launch
                }
            }

            // 2) Save profile with possibly-updated picture URL
            val profileToSave = userProfile.copy(
                userId = userId,
                profilePicture = finalProfilePicture
            )

            when (val saveResult = userProfileUseCase.saveUserProfile(profileToSave)) {
                is Result.Success -> {
                    _userProfile.value = Result.Success(profileToSave)
                }

                is Result.Error -> {
                    _userProfile.value = Result.Error(saveResult.message)
                }

                else -> {
                    _userProfile.value = Result.Idle
                }
            }
        }
    }
}
