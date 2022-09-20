package com.io.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class SendMessageRequest(
    val text: String
)