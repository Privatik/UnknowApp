package com.io.data.internal.datasource

import com.io.data.api.datasource.TokenDataSource
import com.io.domain.model.Tokens

class TokenDataSourceImpl: TokenDataSource {

    override var tokens: Tokens? = null
        get() = field
        set(value) {field = value}
}