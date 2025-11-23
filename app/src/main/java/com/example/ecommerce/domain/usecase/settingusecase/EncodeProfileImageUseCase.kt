package com.example.ecommerce.domain.usecase.settingusecase

import com.example.ecommerce.domain.repository.UserProfileRepository
import javax.inject.Inject

class EncodeProfileImageUseCase @Inject constructor(
    private val repository: UserProfileRepository
){

    suspend operator fun invoke(bytes: ByteArray): String {
        return repository.encodeToBase64(bytes)
    }
}