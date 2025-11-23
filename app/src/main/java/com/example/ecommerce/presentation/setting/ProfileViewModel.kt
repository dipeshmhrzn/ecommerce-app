package com.example.ecommerce.presentation.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerce.domain.model.UserProfile
import com.example.ecommerce.domain.usecase.authusecase.GetCurrentUserIdUseCase
import com.example.ecommerce.domain.usecase.settingusecase.UserProfileUseCase
import com.example.ecommerce.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userProfileUseCase: UserProfileUseCase,
    private val getCurrentUserIdUseCase: GetCurrentUserIdUseCase
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

    fun saveUserProfile(userProfile: UserProfile) {
        viewModelScope.launch {
            _userProfile.value = Result.Loading
            getCurrentUserIdUseCase()?.let { userId ->
                val result = userProfileUseCase.saveUserProfile(userProfile.copy(userId = userId))
                when (result) {
                    is Result.Success -> {
                        _userProfile.value =Result.Success(userProfile) // Update state with saved profile
                    }

                    is Result.Error -> {
                        _userProfile.value = Result.Error(result.message) // Show error message
                    }

                    else -> {
                        _userProfile.value = Result.Idle // Handle idle state if needed
                    }

                }
            }
        }
    }
}
