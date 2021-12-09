package com.io.unknow

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class UnknownApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}