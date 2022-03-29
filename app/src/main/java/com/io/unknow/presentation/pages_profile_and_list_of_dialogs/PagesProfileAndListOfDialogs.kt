package com.io.unknow.presentation.pages_profile_and_list_of_dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.io.unknow.presentation.list_of_dialogs.ListOfDialogsScreen
import com.io.unknow.presentation.profile.ProfileScreen

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PagesProfileAndListOfDialogs(
    navController: NavController
) {
    val pagerCount = 2
    HorizontalPager(
        count = pagerCount,
        modifier = Modifier.fillMaxWidth()
    ) { page ->
        if (page == 0) ProfileScreen(navController)
        else ListOfDialogsScreen(navController)
    }
}