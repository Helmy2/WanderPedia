package com.example.wanderpedia.core.domain.repository

import android.content.Context
import com.example.wanderpedia.core.domain.model.Resource
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

interface CredentialRepository {
    suspend fun createGoogleCredential(context: Context): Resource<GoogleIdTokenCredential>
}