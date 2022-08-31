package com.io.unknow.presentation.util

sealed class Screen(val route: String){
    object SplashScreen: Screen("splash_screen")
    object LoginScreen: Screen("login_screen")
    object RegisterScreen: Screen("register_screen")
    object ChatScreen: Screen("chat_screen")
}
