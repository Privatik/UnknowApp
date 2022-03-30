package com.io.unknow.presentation.chat

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.domain.model.Message
import com.io.domain.usecase.ChatUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class ChatState(
    val messageText: String = "",
    val pictureList: List<String> = emptyList(),
    val messages: List<Message> = emptyList()
)

class ChatViewModel: ViewModel() {
    private val userCase: ChatUseCase = ChatUseCase()

    private val _state = mutableStateOf(ChatState())
    val state: State<ChatState> = _state

    init {
        userCase
            .messages
            .onEach {

            }
            .launchIn(viewModelScope)
    }

    fun sendMessage(message: Message){
        viewModelScope.launch {
            userCase(message)
        }
    }
}