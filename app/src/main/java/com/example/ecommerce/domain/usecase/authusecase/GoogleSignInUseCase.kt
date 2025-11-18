package com.example.ecommerce.domain.usecase.authusecase

import com.example.ecommerce.domain.repository.AuthRepository
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String) = authRepository.signInWithGoogle(idToken)
}