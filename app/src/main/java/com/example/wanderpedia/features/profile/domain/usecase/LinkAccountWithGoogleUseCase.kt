package com.example.wanderpedia.features.profile.domain.usecase

import com.example.wanderpedia.core.domain.repository.UserRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject

class LinkAccountWithGoogleUseCase @Inject constructor(
    private val userRepo: UserRepository
) {
    suspend operator fun invoke(credential: GoogleIdTokenCredential) =
        userRepo.linkAccountWithGoogle(credential)
}