package com.io.unknow.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.io.unknow.presentation.chat.components.Message
import com.io.unknow.presentation.components.StandardTextField
import com.io.unknow.presentation.ui.theme.SpaceMedium
import com.io.unknow.presentation.util.factory
import io.pagination.common.rememberPagingLazyListState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun ChatScreen(
    onExit:() -> Unit,
    viewModel: ChatViewModel,
){
    val state by viewModel.state.collectAsState()
    val lazyScrollState = rememberPagingLazyListState(
        nextPageLoading = viewModel::actionLoadNextPage,
        previousPageLoading = viewModel::actionLoadPreviousPage
    )

    LaunchedEffect(Unit){
        viewModel.effect
            .onEach {
                onExit()
            }
            .launchIn(this)
    }


    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.primary)
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
            IconButton(onClick = viewModel::actionReturn) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "returnToLogicScreen")
            }
            Spacer(modifier = Modifier.width(SpaceMedium))
            Text(
                textAlign = TextAlign.Center,
                text = "test"
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .background(MaterialTheme.colors.background)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyScrollState,
                horizontalAlignment = Alignment.CenterHorizontally,
                reverseLayout = true
            ){
                items(
                    items = state.messages,
                    key = { it.id }
                ) {
                    Message(isMyMessage = it.userId == state.userId, message = it)
                }
                if (state.isLoadingNewMessage){
                    item {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 5.dp
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
        ) {
            IconButton(onClick = {}) {
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