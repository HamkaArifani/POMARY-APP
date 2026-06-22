package com.example.pomaryapp.core.di

import com.example.pomaryapp.data.repository.AuthRepositoryImpl
import com.example.pomaryapp.data.repository.PreorderRepositoryImpl
import com.example.pomaryapp.domain.repository.AuthRepository
import com.example.pomaryapp.domain.repository.PreorderRepository
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
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindPreorderRepository(
        preorderRepositoryImpl: PreorderRepositoryImpl
    ): PreorderRepository

}