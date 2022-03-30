package com.io.unknow.presentation.util

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.io.unknow.R
import com.io.unknow.presentation.chat.ChatScreen
import com.io.unknow.presentation.login.LoginScreen
import com.io.unknow.presentation.pages_profile_and_list_of_dialogs.PagesProfileAndListOfDialogs
import com.io.unknow.presentation.register.RegisterScreen
import com.io.unknow.presentation.splash.SplashScreen
import com.io.unknow.util.extends.changeColorStatusBar

@Composable
fun Navigation(activity: ComponentActivity){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.ChatScreen.route){

        composable(Screen.SplashScreen.route){
            SplashScreen(navController = navController)
        }

        composable(Screen.LoginScreen.route){
            LoginScreen(navController = navController)
            activity.changeColorStatusBar(R.color.ebony_clay)
        }
        composable(Screen.RegisterScreen.route){
            RegisterScreen(navController = navController)
        }
        composable(Screen.PagesProfileAndListOfDialogsScreen.route){
            PagesProfileAndListOfDialogs(navController = navController)
            activity.changeColorStatusBar(R.color.steel_gray)
        }
        composable(Screen.ChatScreen.route){
            ChatScreen(navController = navController)
            activity.changeColorStatusBar(R.color.steel_gray)
        }

    }
}