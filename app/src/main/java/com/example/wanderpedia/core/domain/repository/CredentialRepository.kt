package com.example.wanderpedia.core.domain.repository

import android.content.Context
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

interface CredentialRepository {
    suspend fun createGoogleCredential(context: Context): Result<GoogleIdTokenCredential>
}