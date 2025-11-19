package com.example.ecommerce.domain.usecase.authusecase

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.example.ecommerce.domain.repository.AuthRepository
import javax.inject.Inject
import com.example.ecommerce.domain.util.Result


class LogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val credentialManager: CredentialManager
) {
    suspend operator fun invoke(): Result<String> {
        return try {
            // 1. Clear Firebase session
            val result = authRepository.logout()
            if (result is Result.Error) return result

            // 2. Clear Google federated credentials (safe even if not Google sign-in)
            try {
                credentialManager.clearCredentialState(ClearCredentialStateRequest())
            } catch (_: Exception) {
                // Ignore: means no Google credentials stored
            }

            Result.Success("Logout successful")

        } catch (e: Exception) {
            Result.Error(e.localizedMessage ?: "Logout failed")
        }
    }
}