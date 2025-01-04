package com.example.wanderpedia.features.auth.di

import com.example.wanderpedia.features.auth.data.repository.GoogleCredentialRepositoryImpl
import com.example.wanderpedia.features.auth.domain.repository.CredentialRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCredentialRepository(
        googleCredentialRepositoryImpl: GoogleCredentialRepositoryImpl
    ): CredentialRepository
}
