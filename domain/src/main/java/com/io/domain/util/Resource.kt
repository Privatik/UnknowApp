package com.io.domain.util

sealed class Resource<out T>{

    class Success<T>(data: T) : Resource<T>()

    class Error(message: Exception) : Resource<Nothing>()

    object Loading : Resource<Nothing>()

}
