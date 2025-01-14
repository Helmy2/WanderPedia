package com.example.wanderpedia.core.domain.usecase

import android.content.Context
import com.example.wanderpedia.core.domain.repository.CredentialRepository
import javax.inject.Inject

class GetGoogleCredentialUseCase @Inject constructor(
    private val credentialRepository: CredentialRepository
) {
    suspend operator fun invoke(context: Context) =
        credentialRepository.createGoogleCredential(context)
}