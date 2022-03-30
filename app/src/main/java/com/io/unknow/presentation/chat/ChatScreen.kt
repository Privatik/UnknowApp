package com.io.unknow.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.io.unknow.presentation.components.StandardTextField
import com.io.unknow.presentation.register.RegisterViewModel
import com.io.unknow.presentation.ui.theme.SpaceMedium
import com.io.unknow.presentation.util.Screen

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    val lazyScrollState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "add")
            }
            Spacer(modifier = Modifier.width(SpaceMedium))
            Text(
                textAlign = TextAlign.Center,
                text = "test"
            )
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = lazyScrollState,
            reverseLayout = true
        ){
            item {
                Text(text = "fffff")
            }
            item {
                Text(text = "next")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
        ) {
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "add")
            }
            Spacer(modifier = Modifier.width(SpaceMedium))
            StandardTextField(
                text = "fdfdf",
                hint = "Message",
                maxLength = 250,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(SpaceMedium))
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "add")
            }
        }
    }
}