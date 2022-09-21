package com.io.domain.usecase

import com.io.domain.model.MessageDTO
import com.io.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.launch
import java.util.*

class ChatInteractor(
    private val chatRepository: ChatRepository,
) {
    private val idSet = hashSetOf<String>()
    val messagesFLow: Flow<List<MessageDTO>> = chatRepository.messagesFLow
        .scan(LinkedList<MessageDTO>()){ list, messages ->
            messages.onSuccess { newList ->
                newList.forEach { item ->
                    if (idSet.contains(item.id)){
                        return@forEach
                    }
                    idSet.add(item.id)
                    when{
                        list.isEmpty() -> {
                            list.add(item)
                        }
                        list.first.timeSend <= item.timeSend -> {
                            list.addFirst(item)
                        }
                        list.last.timeSend >= item.timeSend -> {
                            list.addLast(item)
                        }
                    }
                }
            }
            list
        }

    suspend fun send(text: String){
        chatRepository.sendMessage(text)
    }

    suspend fun initPage() {
        chatRepository.refreshPage()
    }

    suspend fun actionLoadNextPage() {
       chatRepository.actionLoadNextPage()
    }

    suspend fun actionLoadPreviousPage() {
        chatRepository.actionLoadPreviousPage()
    }

}