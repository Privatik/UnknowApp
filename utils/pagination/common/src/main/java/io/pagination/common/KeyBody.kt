package io.pagination.common

data class KeyBody<Key: Any>(
    val lastPage: Key?,
    val nextPage: Key?
)
