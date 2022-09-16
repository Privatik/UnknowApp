package com.io.unknow

import android.app.Application
import com.io.data.di.DataServiceLocator

class UnknownApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        DataServiceLocator.instance(this)
    }
}