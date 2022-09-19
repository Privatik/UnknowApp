package com.io.data.repository

import com.io.data.di.DataServiceLocator
import com.io.data.paging.MessagePagination
import com.io.data.remote.MessageApi
import com.io.data.remote.MessageResponse
import com.io.data.remote.ResponseBody
import com.io.data.remote.implMessageApi
import com.io.data.storage.DataStorage
import com.io.data.storage.KeyForUserId
import com.io.data.storage.KeyForUserName
import com.io.data.storage.userIdKey
import com.io.domain.model.MessageDTO
import com.io.domain.repository.ChatRepository
import io.pagination.common.PagingAdapter
import kotlinx.coroutines.flow.*
import java.util.*

fun implChatRepository(): ChatRepository{
    val instance = DataServiceLocator.instance()
    return ChatRepositoryImpl(instance.dataStorage, implMessageApi())
}

class ChatRepositoryImpl(
    private val dataStore: DataStorage,
    private val messageApi: MessageApi,
    private val paginator: PagingAdapter<Int, Result<ResponseBody<List<MessageResponse>>>> = PagingAdapter(MessagePagination(messageApi))
): ChatRepository {
    override val messagesFLow: Flow<List<MessageDTO>> =
        merge(
            messageApi.socketMessage().transform(),
            paginator.data { response -> response.map { it.message } }
        )
        .scan(LinkedList<MessageDTO>()){ list, messages ->
            messages.onSuccess { newList ->
                list.addAll(newList.map { it.asDTO() })
            }
            list
        }

    override suspend fun sendMessage(text: String): Result<Boolean> {
        val userId = dataStore.secureData(
            keyAlias = userIdKey,
            fetchValue = { it[KeyForUserId].orEmpty() },
            mapper = { it }
        ).first()

        val userName = dataStore.data.map { it[KeyForUserName].orEmpty() }.first()
        return messageApi.send(userId, userName, text)
            .map { it.isSuccessful }
    }

    override suspend fun refreshPage(initPage: Int) = paginator.actionRefresh(initPage)
    override suspend fun actionLoadNextPage() = paginator.actionLoadNext()
    override suspend fun actionLoadPreviousPage() = paginator.actionLoadPrevious()


    private fun Flow<MessageApi.ResponseFromMessageSocket>.transform(): Flow<Result<List<MessageResponse>>>{
        return map { apiAnswer ->
            when(apiAnswer){
                MessageApi.ResponseFromMessageSocket.DontAuth -> {
                    messageApi.getMessages(0, 20).map {
                        it.message
                    }
                }
                is MessageApi.ResponseFromMessageSocket.NewMessage -> {
                    messageApi.getMessage(apiAnswer.id).map {
                        listOf(it.message)
                    }
                }
                MessageApi.ResponseFromMessageSocket.Exit -> {
                    Result.success(emptyList())
                }
            }
        }
    }

}