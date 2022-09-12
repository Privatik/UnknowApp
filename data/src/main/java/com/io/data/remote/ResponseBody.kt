package com.io.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class ResponseBody<T>(
    val isSuccessful: Boolean,
    val message: T
)
