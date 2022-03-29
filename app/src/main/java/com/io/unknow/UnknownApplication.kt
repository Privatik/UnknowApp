package com.io.unknow

import android.app.Application
import com.io.unknow.di.AppComponent
import com.io.unknow.di.DaggerAppComponent

class UnknownApplication: Application() {

    private var _appComponent: AppComponent? = null
    val appComponent: AppComponent
        get() = _appComponent!!

    override fun onCreate() {
        super.onCreate()

        _appComponent = DaggerAppComponent
            .builder()
            .build()
    }
}