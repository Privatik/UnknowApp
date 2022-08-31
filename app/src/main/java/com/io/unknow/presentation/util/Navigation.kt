package com.io.unknow.presentation.util

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.io.unknow.presentation.chat.ChatScreen
import com.io.unknow.presentation.login.LoginScreen
import com.io.unknow.presentation.register.RegisterScreen
import com.io.unknow.presentation.splash.SplashScreen

@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.ChatScreen.route){

        composable(Screen.SplashScreen.route){
            SplashScreen{
                navController.popBackStack()
                navController.navigate(it)
            }
        }

        composable(Screen.LoginScreen.route){
            LoginScreen(
                openNextScreen = {
                    navController.navigate(it)
                }
            )
        }
        composable(Screen.RegisterScreen.route){
            RegisterScreen(navController = navController)
        }

        composable(Screen.ChatScreen.route){
            ChatScreen(navController = navController)
        }

    }
}