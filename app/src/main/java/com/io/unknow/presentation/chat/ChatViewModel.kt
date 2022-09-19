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
    private val paginatorUseCase: PagingAdapter<Int, List<MessageDTO>> = PagingAdapter(MessagePagination(
        implMessageApi()
    )),
    private val sendMassageUseCase: ChatInteractor = ChatInteractor(implChatRepository()),
    private val userRepository: UserRepository = implUserRepository(),
): ViewModel() {

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ChatEffect>()
    val effect: SharedFlow<ChatEffect> = _effect.asSharedFlow()

    init {
        initUserId()
//        initPage()
        listenerLoadPage()
    }

    private fun initPage() = viewModelScope.launch{
        paginatorUseCase.actionRefresh(0)
    }

    private fun initUserId() = viewModelScope.launch{
        _state.emit(_state.value.copy(userId = userRepository.userId.first()))
    }

    private fun listenerLoadPage(){
//        paginatorUseCase.listener(
//            scope = viewModelScope,
//            onSuccess = {
//                val list = it.map { dto -> dto.asUI() }
//                val oldMessage = _state.value.messages
//                val newMessage = if ((oldMessage.lastOrNull()?.time ?: -1) > (list.firstOrNull()?.time ?: -1)) {
//                    list + oldMessage
//                } else {
//                    oldMessage + list
//                }
//                _state.emit(
//                    _state.value.copy(messages = newMessage)
//                )
//            },
//            onFailure = {
//
//            }
//        )

        sendMassageUseCase.messagesFLow.map { Result.success(it) }
            .onEach {
                if (it.isSuccess){
                    val list = it.getOrDefault(emptyList()).map { dto -> dto.asUI() }
                    val oldMessage = _state.value.messages
                    val newMessage = if ((oldMessage.lastOrNull()?.time ?: -1) > (list.firstOrNull()?.time ?: -1)) {
                        oldMessage + list
                    } else {
                        list + oldMessage
                    }.toSet().toList()
                    _state.emit(
                        _state.value.copy(messages = newMessage)
                    )
                }
            }
            .flowOn(Dispatchers.IO)
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
        userRepository.logout()
        _effect.emit(ChatEffect.OnBack)
    }

    fun sendMessage() = viewModelScope.launch {
        sendMassageUseCase.send(state.value.messageText)
        _state.emit(state.value.copy(messageText = ""))
    }
}