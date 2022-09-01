package com.io.unknow.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.io.domain.model.SendMessageDO
import com.io.domain.usecase.ChatUseCase
import com.io.domain.usecase.SendMessageUseCase
import com.io.unknow.presentation.chat.model.MessageUI
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

data class ChatState(
    val messageText: String = "",
    val messages: List<MessageUI> = emptyList()
)

sealed class ChatEffect{
    object OnBack: ChatEffect()
}

class ChatViewModel(
    private val chatUseCase: ChatUseCase = ChatUseCase(),
    private val sendMassageUseCase: SendMessageUseCase = SendMessageUseCase(),
    private val id: String
): ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ChatEffect>()
    val effect: SharedFlow<ChatEffect> = _effect.asSharedFlow()

    init {
        chatUseCase()
            .onEach {

            }
            .launchIn(viewModelScope)
    }

    fun setMessageText(text: String) = viewModelScope.launch{
        _state.emit(_state.value.copy(messageText = text))
    }

    fun actionReturn() = viewModelScope.launch{
        _effect.emit(ChatEffect.OnBack)
    }

    fun sendMessage(){
        viewModelScope.launch {
            sendMassageUseCase(SendMessageDO(id, state.value.messageText))
                .onSuccess {
                    val oldMessages = state.value.messages
                    _state.emit(state.value.copy(messages = (oldMessages + MessageUI("",state.value.messageText, 11)), messageText = ""))
                }
                .onFailure {

                }
        }
    }
}