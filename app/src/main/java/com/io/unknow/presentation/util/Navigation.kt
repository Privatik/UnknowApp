package com.io.unknow.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.io.data.di.DataServiceLocator
import com.io.data.repository.impl
import com.io.unknow.presentation.chat.ChatScreen
import com.io.unknow.presentation.chat.ChatViewModel
import com.io.unknow.presentation.login.LoginScreen
import com.io.unknow.presentation.register.RegisterScreen
import com.io.unknow.presentation.splash.SplashScreen
import com.io.unknow.presentation.test_screen.TestScreen
import com.io.unknow.presentation.test_screen.TestViewModel

@Composable
fun Navigation(){
    val navController = rememberNavController()


    NavHost(
        navController = navController,
        startDestination = Screen.TestScreen.route){

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
            RegisterScreen(
                onBack = {
                    if (it == null){
                        navController.popBackStack()
                    }
                },
                openNextScreen = {
                    navController.navigate(it)
                }
            )
        }

        composable(Screen.ChatScreen.route){
            val viewModel = viewModel<ChatViewModel>(factory = factory(ChatViewModel(id = "")))
            ChatScreen(
                onExit = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable(Screen.TestScreen.route){
            val context = LocalContext.current
            val viewModel = viewModel<TestViewModel>(
                factory = factory(
                    TestViewModel(impl(context.applicationContext))
                )
            )
            TestScreen(viewModel = viewModel)
        }

    }
}