package com.io.data.internal.mapper

import com.io.data.internal.model.UserResponse
import com.io.domain.model.User

fun UserResponse.toModel(): User = User(
    id = this.id,
    email = this.email,
    height = this.height,
    weight = this.weight,
    locate = this.locate,
    sex = this.sex.parse(),
    birthDay = this.birthDay
)