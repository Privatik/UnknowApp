package com.io.data.di

import androidx.activity.ComponentActivity
import dagger.Component

@Component(dependencies = [RepositoryModule::class, DataSourceModule::class, UseCaseModule::class])
interface LoginComponent {

    fun inject(activity: ComponentActivity)

    @Component.Builder
    interface Builder {
        fun build(): LoginComponent
    }
}