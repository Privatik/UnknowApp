package com.io.unknow.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.data.paging.MessagePagination
import com.io.data.remote.implMessageApi
import com.io.data.repository.implChatRepository
import com.io.data.repository.implUserRepository
import com.io.domain.model.MessageDTO
import com.io.domain.repository.UserRepository
import com.io.domain.usecase.ChatInteractor
import com.io.unknow.presentation.chat.model.MessageUI
import com.io.unknow.presentation.chat.model.asUI
import io.pagination.common.PagingAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ChatState(
    val userId: String = "",
    val messageText: String = "",
    val messages: List<MessageUI> = mutableListOf(),
    val isLoadingNewMessage: Boolean = false
)

sealed class ChatEffect{
    object OnBack: ChatEffect()
}

class ChatViewModel(
    private val sendMassageUseCase: ChatInteractor = ChatInteractor(implChatRepository()),
    private val userRepository: UserRepository = implUserRepository(),
): ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ChatEffect>()
    val effect: SharedFlow<ChatEffect> = _effect.asSharedFlow()

    init {
        initUserId()
        initPage()
        listenerLoadPage()
    }

    private fun initPage() = viewModelScope.launch(Dispatchers.IO){
        sendMassageUseCase.initPage()
    }

    private fun initUserId() = viewModelScope.launch{
        _state.emit(_state.value.copy(userId = userRepository.userId.first()))
    }

    private fun listenerLoadPage(){
        sendMassageUseCase.messagesFLow
            .onEach { list ->
                _state.emit(
                    _state.value.copy(messages = list.map { it.asUI() })
                )
            }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun actionLoadNextPage() = viewModelScope.launch {
        _state.emit(_state.value.copy(isLoadingNewMessage = true))
        sendMassageUseCase.actionLoadNextPage()
        _state.emit(_state.value.copy(isLoadingNewMessage = false))
    }

    fun actionLoadPreviousPage() = viewModelScope.launch {
        sendMassageUseCase.actionLoadPreviousPage()
    }

    fun setMessageText(text: String) = viewModelScope.launch{
        _state.emit(_state.value.copy(messageText = text))
    }

    fun actionReturn() = viewModelScope.launch{
        userRepository.logout()
        _effect.emit(ChatEffect.OnBack)
    }

    fun sendMessage() = viewModelScope.launch {
        val message = state.value.messageText
        _state.emit(state.value.copy(messageText = ""))
        sendMassageUseCase.send(message)
    }
}