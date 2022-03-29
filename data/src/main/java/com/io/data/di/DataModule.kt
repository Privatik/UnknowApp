package com.io.data.di

import com.io.data.internal.repository.LoginRepositoryImpl
import com.io.domain.repository.LoginRepository
import com.io.domain.usecase.AuthorizationUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [RepositoryModule::class])
class UseCaseModule {

    @Provides
    fun provideAuthUseCase(loginRepository: LoginRepository): AuthorizationUseCase{
        return AuthorizationUseCase(loginRepository)
    }


}

@Module
abstract class RepositoryModule{

    @Binds
    abstract fun provideLoginRepository(repository: LoginRepositoryImpl): LoginRepository
}

