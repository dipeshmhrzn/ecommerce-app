package com.example.ecommerce.domain.usecase.authusecase

import com.example.ecommerce.domain.repository.AuthRepository
import com.example.ecommerce.domain.util.Result
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class GoogleSignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<FirebaseUser>{
       return authRepository.signInWithGoogle(idToken)
    }
}