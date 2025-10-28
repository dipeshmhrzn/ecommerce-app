package com.example.ecommerce.domain.usecase.authusecase

import android.util.Patterns
import com.example.ecommerce.domain.repository.AuthRepository
import com.example.ecommerce.domain.util.Result
import com.example.ecommerce.domain.util.ValidationErrors
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String, confirmPassword: String): Result<String> {

        if (email.isBlank()){
            return Result.Error(ValidationErrors.EmailError("Email cannot be empty"))
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return Result.Error(ValidationErrors.EmailError("Invalid Email Format"))
        }
        if (password.length < 6) {
            return Result.Error(ValidationErrors.PasswordError("Password must be at least 6 characters"))
        }
        if (password!=confirmPassword){
            return Result.Error(ValidationErrors.ConfirmPasswordError("Password didn't match"))
        }
        return repository.signup(email, password)
    }
}