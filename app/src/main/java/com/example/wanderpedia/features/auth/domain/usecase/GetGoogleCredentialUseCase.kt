package com.example.wanderpedia.features.auth.domain.usecase

import android.content.Context
import com.example.wanderpedia.features.auth.domain.repository.CredentialRepository
import javax.inject.Inject

class GetGoogleCredentialUseCase @Inject constructor(
    private val credentialRepository: CredentialRepository
) {
    suspend operator fun invoke(context: Context) =
        credentialRepository.createGoogleCredential(context)
}