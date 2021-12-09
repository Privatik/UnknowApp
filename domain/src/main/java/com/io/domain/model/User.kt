package com.io.domain.model

import com.io.domain.state.Sex

data class User(
    val id: String,
    val email: String,
    val height: Int? = null,
    val weight: Int? = null,
    val locate: String? = null,
    val sex: Sex,
    val birthDay: Long
)
