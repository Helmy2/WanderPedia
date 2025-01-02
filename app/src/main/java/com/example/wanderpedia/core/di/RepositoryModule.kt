package com.example.wanderpedia.core.di

import com.example.wanderpedia.core.data.repository.UserRepositoryImpl
import com.example.wanderpedia.core.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        auth: FirebaseAuth,
    ): UserRepository =
        UserRepositoryImpl(
            auth = auth,
        )
}