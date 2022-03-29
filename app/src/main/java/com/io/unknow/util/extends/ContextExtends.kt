package com.io.unknow.util.extends

import android.content.Context
import com.io.unknow.UnknownApplication
import com.io.unknow.di.AppComponent

val Context.compat: AppComponent
    get() {
        return (applicationContext as UnknownApplication).appComponent
    }

