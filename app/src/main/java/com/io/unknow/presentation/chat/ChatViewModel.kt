package com.io.unknow.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.data.paging.MessagePagination
import com.io.domain.model.SendMessageDO
import com.io.domain.usecase.ChatInteractor
import com.io.unknow.presentation.chat.model.MessageUI
import io.pagination.common.PaginatorInteractor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

data class ChatState(
    val messageText: String = "",
    val messages: List<MessageUI> = emptyList(),
    val isLoadingNewMessage: Boolean = false
)

sealed class ChatEffect{
    object OnBack: ChatEffect()
}

class ChatViewModel(
    private val paginatorUseCase: PaginatorInteractor<Int, List<String>> = PaginatorInteractor(MessagePagination()),
    private val sendMassageUseCase: ChatInteractor = ChatInteractor(),
    private val id: String
): ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ChatEffect>()
    val effect: SharedFlow<ChatEffect> = _effect.asSharedFlow()

    init {
        println("Paging ini")
        initPage()
        listenerLoadPage()
    }

    private fun initPage() = viewModelScope.launch{
        paginatorUseCase.actionRefresh(1)
    }

    private fun listenerLoadPage(){
        paginatorUseCase.data
            .onEach {
                if (it.isSuccess){
                    val list = it.getOrDefault(emptyList())
                    val oldMessage = _state.value.messages
                    _state.emit(
                        _state.value.copy(messages = oldMessage + list.map { text ->
                            MessageUI(UUID.randomUUID().toString(), text,1111)
                        })
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun actionLoadNextPage() = viewModelScope.launch {
        _state.emit(_state.value.copy(isLoadingNewMessage = true))
        paginatorUseCase.actionLoadNext()
        _state.emit(_state.value.copy(isLoadingNewMessage = false))
    }

    fun actionLoadPreviousPage() = viewModelScope.launch {
        paginatorUseCase.actionLoadPrevious()
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