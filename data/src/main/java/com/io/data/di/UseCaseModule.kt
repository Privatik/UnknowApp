package com.io.data.di

import com.io.domain.repository.LoginRepository
import com.io.domain.usecase.AuthorizationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun providesAuthUseCase(loginRepository: LoginRepository): AuthorizationUseCase =
        AuthorizationUseCase(loginRepository)
}