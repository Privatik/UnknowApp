package com.io.domain.usecase

import com.io.domain.model.SendMessageDO

class SendMessageUseCase {

    suspend operator fun invoke(messageDO: SendMessageDO): Result<Boolean>{
        return Result.success(true)
    }
}