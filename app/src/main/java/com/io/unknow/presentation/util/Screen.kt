package com.io.unknow.presentation.util

sealed class Screen(val route: String){
    object SplashScreen: Screen("splash_screen")
    object LoginScreen: Screen("login_screen")
    object RegisterScreen: Screen("register_screen")
    object PagesProfileAndListOfDialogsScreen: Screen("profile_and_list_of_dialogs_screen")
}
