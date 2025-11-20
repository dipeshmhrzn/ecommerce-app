package com.example.ecommerce.domain.usecase.authusecase

import com.example.ecommerce.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserIdUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): String? {
        return repository.getCurrentUserId()
    }
}