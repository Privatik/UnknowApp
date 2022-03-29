package com.io.unknow.di

import com.io.data.di.RepositoryModule
import com.io.data.di.UseCaseModule
import com.io.unknow.presentation.MainActivity
import dagger.Component

@Component(modules = [UseCaseModule::class, RepositoryModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
}