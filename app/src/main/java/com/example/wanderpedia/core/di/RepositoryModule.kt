package com.example.wanderpedia.core.di

import com.example.wanderpedia.core.data.repository.UserRepositoryImpl
import com.example.wanderpedia.core.data.repository.WondersRepositoryImpl
import com.example.wanderpedia.core.domain.repository.UserRepository
import com.example.wanderpedia.core.domain.repository.WondersRepository
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
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindWonderRepository(
        wondersRepositoryImpl: WondersRepositoryImpl
    ): WondersRepository
}
