package com.io.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.io.data.di.DataServiceLocator
import com.io.data.remote.MessageApi
import com.io.data.remote.MessageResponse
import com.io.data.remote.implMessageApi
import com.io.data.storage.KeyForUserId
import com.io.data.storage.KeyForUserName
import com.io.domain.model.MessageDTO
import com.io.domain.repository.ChatRepository
import kotlinx.coroutines.flow.*
import java.util.*

fun implChatRepository(): ChatRepository{
    val instance = DataServiceLocator.instance()
    return ChatRepositoryImpl(instance.dataStore, implMessageApi())
}

class ChatRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val messageApi: MessageApi
): ChatRepository {
    override val messagesFLow: Flow<List<MessageDTO>> = messageApi.socketMessage()
        .map { apiAnswer ->
            when(apiAnswer){
                MessageApi.AnswerFromMessageSocket.DontAuth -> {
                    messageApi.getMessages(0, 20).map {
                        it.message
                    }
                }
                is MessageApi.AnswerFromMessageSocket.NewMessage -> {
                    messageApi.getMessage(apiAnswer.id).map {
                        listOf(it.message)
                    }
                }
                MessageApi.AnswerFromMessageSocket.Exit -> {
                    Result.success(emptyList())
                }
            }
        }
        .scan(LinkedList<MessageDTO>()){ list, messages ->
            messages.onSuccess { newList ->
                list.addAll(newList.sortedByDescending { it.timeSend }.map { it.asDTO() })
            }
            list
        }

    override suspend fun sendMessage(text: String): Result<Boolean> {
        val userId = dataStore.data.map { it[KeyForUserId].orEmpty() }.first()
        val userName = dataStore.data.map { it[KeyForUserName].orEmpty() }.first()
        return messageApi.send(userId, userName, text)
            .map { it.isSuccessful }
    }

}