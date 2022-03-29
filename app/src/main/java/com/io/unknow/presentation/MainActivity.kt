package com.io.unknow.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.io.unknow.presentation.ui.theme.UnknowAppTheme
import com.io.unknow.presentation.util.Navigation
import com.io.unknow.util.extends.compat

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compat.inject(this)

        setContent {
            UnknowAppTheme {
                Surface(
                    color = MaterialTheme.colors.background,
                    modifier = Modifier.fillMaxSize()) {
                        Navigation(this)
                }
            }
        }
    }
}