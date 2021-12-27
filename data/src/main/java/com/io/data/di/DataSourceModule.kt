package com.io.data.di

import com.io.data.api.datasource.LoginDataSource
import com.io.data.api.datasource.TokenDataSource
import com.io.data.internal.datasource.TokenDataSourceImpl
import com.io.data.internal.repostory.LoginRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindTokenDataSource(dataSource: TokenDataSourceImpl):TokenDataSource

}