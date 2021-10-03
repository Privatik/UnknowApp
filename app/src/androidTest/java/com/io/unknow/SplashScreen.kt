package com.io.unknow

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.NavController
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.io.unknow.presentation.MainActivity
import com.io.unknow.presentation.splash.SplashScreen
import com.io.unknow.presentation.theme.UnknowAppTheme
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.internal.configuration.MockAnnotationProcessor

@RunWith(AndroidJUnit4::class)
class SplashScreen {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()


    @RelaxedMockK
    lateinit var navController: NavController

    @Before
    fun setUp(){
        MockKAnnotations.init(this)
    }

    @Test
    fun splashScreen_displaysAndDisappears() {
        composeTestRule.setContent {
            UnknowAppTheme{
                SplashScreen(navController = navController)
            }
        }

        composeTestRule
            .onNodeWithContentDescription("Logo")
            .assertExists()
    }
}