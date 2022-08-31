package com.io.domain.usecase

import com.io.domain.model.MessageDO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ChatUseCase {

    operator fun invoke(): Flow<List<MessageDO>> {
        return flowOf()
    }
}