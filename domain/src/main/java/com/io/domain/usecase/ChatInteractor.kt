package com.io.domain.usecase

import com.io.domain.model.MessageDTO
import com.io.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ChatInteractor(
    private val chatRepository: ChatRepository,
) {
    val messagesFLow: Flow<List<MessageDTO>> = chatRepository.messagesFLow

    suspend fun initPage() = viewModelScope.launch{
        paginatorUseCase.actionRefresh(0)
    }

    suspend fun send(text: String): Result<Boolean>{
        return chatRepository.sendMessage(text)
    }

    suspend fun actionLoadNextPage() = viewModelScope.launch {
        _state.emit(_state.value.copy(isLoadingNewMessage = true))
        paginatorUseCase.actionLoadNext()
        _state.emit(_state.value.copy(isLoadingNewMessage = false))
    }

    suspend fun actionLoadPreviousPage() = viewModelScope.launch {
        paginatorUseCase.actionLoadPrevious()
    }

}