package com.io.unknow.presentation.pages_profile_and_list_of_dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.io.unknow.presentation.list_of_dialogs.ListOfDialogsScreen
import com.io.unknow.presentation.profile.ProfileScreen

@Composable
fun PagesProfileAndListOfDialogs(
    navController: NavController
){
//    LazyRow(modifier = Modifier.fillMaxSize()){
//        item {
//            ProfileScreen()
//        }
//        item {
//            ListOfDialogsScreen()
//        }
//    }
    Text(
        text = "THIS",
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxSize())
}