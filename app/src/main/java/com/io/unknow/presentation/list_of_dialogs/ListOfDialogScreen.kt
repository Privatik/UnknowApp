package com.io.unknow.presentation.list_of_dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.io.unknow.R
import com.io.unknow.presentation.list_of_dialogs.components.CollapsingToolbarWithOutlinedTextFiled
import com.io.unknow.presentation.list_of_dialogs.components.ItemDialog
import com.io.unknow.presentation.ui.theme.Shadow

@Composable
fun ListOfDialogsScreen(
    navController: NavController,
    viewModel: ListOfDialogViewModel = hiltViewModel()
){
    val toolbarHeight = 80.dp
    CollapsingToolbarWithOutlinedTextFiled(
        searchText = viewModel.searchText.value,
        onChangeSearchText = {
            viewModel.setSearchText(search = it)
        },
        toolbarHeight = toolbarHeight
    ){
        LazyColumn(
            modifier = Modifier
                .padding(PaddingValues(top = toolbarHeight)))
        {
            repeat(30){ index ->
                item {
                    ItemDialog(
                        nameUser = "Alexander",
                        lastMessage = "Hello",
                        isMyMessage = index % 2 == 0,
                        onClick = {}
                    )
                }
            }
        }
    }
}