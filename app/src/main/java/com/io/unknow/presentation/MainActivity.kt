package com.io.unknow.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.io.unknow.presentation.ui.theme.UnknowAppTheme
import com.io.unknow.presentation.util.Navigation

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureEdgeToEdge()

        setContent {
            UnknowAppTheme {
                val sysUiController = rememberSystemUiController()
                SideEffect {
                    sysUiController.setSystemBarsColor(
                        color = Color.Transparent,
                        darkIcons = false,
                    )
                }

                ProvideWindowInsets(consumeWindowInsets = false) {
                    Surface(
                        color = MaterialTheme.colors.background,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Navigation()
                    }
                }
            }
        }
    }

    private fun configureEdgeToEdge() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
//        window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
//        window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
    }

}