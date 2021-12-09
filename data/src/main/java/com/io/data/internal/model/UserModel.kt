package com.io.data.internal.model

import com.io.domain.state.Sex
import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val email: String,
    val password: String,
    val birthDay: Long,
    val sex: Int,
)

@Serializable
data class UserResponse(
    val id: String,
    val email: String,
    val height: Int? = null,
    val weight: Int? = null,
    val locate: String? = null,
    val sex: SexResponse,
    val birthDay: Long
)

@Serializable
enum class SexResponse{
    MAN,
    WOMAN,
    UNKNOWN;

    fun parse(): Sex =
        when(this){
            MAN -> Sex.MAN
            WOMAN -> Sex.WOMAN
            UNKNOWN -> Sex.UNKNOWN
        }
}