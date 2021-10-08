package com.io.unknow.presentation.pages_profile_and_list_of_dialogs

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.io.unknow.presentation.list_of_dialogs.ListOfDialogsScreen
import com.io.unknow.presentation.profile.ProfileScreen
import com.io.unknow.util.Tags
import timber.log.Timber

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
        else ListOfDialogsScreen()
    }
}