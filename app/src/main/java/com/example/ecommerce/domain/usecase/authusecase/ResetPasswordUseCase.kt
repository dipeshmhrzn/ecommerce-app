package com.example.ecommerce.domain.usecase.authusecase

import com.example.ecommerce.domain.repository.AuthRepository
import com.example.ecommerce.domain.util.Result
import com.example.ecommerce.domain.util.ValidationErrors
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String): Result<String>{

        if (email.isBlank()){
            return Result.Error(ValidationErrors.EmailError("Email cannot be empty"))
        }

       return authRepository.resetPassword(email)
    }
}