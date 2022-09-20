package com.io.domain.repository

import com.io.domain.model.MessageDTO
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val messagesFLow: Flow<Result<List<MessageDTO>>>

    suspend fun sendMessage(text: String)
    suspend fun refreshPage(initPage: Int = 0)
    suspend fun actionLoadNextPage()
    suspend fun actionLoadPreviousPage()
}