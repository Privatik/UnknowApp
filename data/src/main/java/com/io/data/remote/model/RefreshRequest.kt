package com.io.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    val userId: String
)
