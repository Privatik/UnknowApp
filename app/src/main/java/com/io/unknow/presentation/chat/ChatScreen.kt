package com.io.unknow.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.io.unknow.presentation.chat.components.Message
import com.io.unknow.presentation.components.StandardTextField
import com.io.unknow.presentation.register.RegisterViewModel
import com.io.unknow.presentation.ui.theme.SpaceMedium
import com.io.unknow.presentation.util.Screen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun ChatScreen(
    onExit:() -> Unit,
    viewModel: ChatViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    val state by viewModel.state.collectAsState()
    val lazyScrollState = rememberLazyListState()

    LaunchedEffect(Unit){
        viewModel.effect
            .onEach {
                onExit()
            }
            .launchIn(this)
    }


    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsWithImePadding()
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
            items(state.messages){
                Message(isMyMessage = true, message = )
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
                text = state.messageText,
                hint = "Message",
                maxLength = 250,
                modifier = Modifier.weight(1f),
                onValueChange = {
                    viewModel.setMessageText(it)
                }
            )
            Spacer(modifier = Modifier.width(SpaceMedium))
            IconButton(onClick = {
                viewModel.sendMessage()
            }) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "add")
            }
        }
    }
}