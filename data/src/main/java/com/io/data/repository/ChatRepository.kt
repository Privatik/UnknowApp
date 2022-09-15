package com.io.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.io.data.remote.MessageApi
import com.io.domain.model.MessageDTO
import com.io.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ChatRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val messageApi: MessageApi
): ChatRepository {
    override val messagesFLow: Flow<List<MessageDTO>> = flow {  }

    override fun sendMessage(text: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override fun logOut(): Result<Boolean> {
        TODO("Not yet implemented")
    }

}