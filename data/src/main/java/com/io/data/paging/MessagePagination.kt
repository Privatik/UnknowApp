package com.io.data.paging

import io.pagination.common.KeyBody
import io.pagination.common.Paginator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map

class MessagePagination: Paginator<Int, List<String>> {
    private val _data = MutableSharedFlow<List<String>>()
    override val data: Flow<Result<List<String>>> = _data.asSharedFlow().map { Result.success(it) }

    override suspend fun refreshPage(initPage: Int): KeyBody<Int> {
        _data.emit((0 until 120).map { "item$it" })
        return KeyBody(null, 4)
    }

    override suspend fun nextPage(pagingBody: Int): Int? {
        if (pagingBody == 10) return null
        delay(2000)
        _data.emit((((pagingBody * 30)) until ((pagingBody + 1) * 30)).map { "item$it" })
        return pagingBody + 1
    }

    override suspend fun previousPage(pagingBody: Int): Int? {
        return null
    }

}