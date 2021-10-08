package com.io.unknow.util.extends

import androidx.activity.ComponentActivity
import androidx.annotation.ColorRes
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat

fun ComponentActivity.changeColorStatusBar(@ColorRes color: Int){
    window.statusBarColor = ContextCompat.getColor(this,color)
}