package com.example.wanderpedia.features.auth.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.example.wanderpedia.R
import com.example.wanderpedia.core.domain.model.Resource
import com.example.wanderpedia.features.auth.domain.repository.CredentialRepository
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import javax.inject.Inject

class GoogleCredentialRepositoryImpl @Inject constructor() : CredentialRepository {

    override suspend fun createGoogleCredential(context: Context): Resource<GoogleIdTokenCredential> {
        return try {
            val signInWithGoogleOption = GetSignInWithGoogleOption
                .Builder(serverClientId = context.getString(R.string.default_web_client_id))
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(signInWithGoogleOption)
                .build()

            val result = CredentialManager.create(context).getCredential(
                request = request,
                context = context
            )

            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
            Resource.Success(googleIdTokenCredential)
        } catch (e: Exception) {
            Resource.Error(e)
        }
    }
}