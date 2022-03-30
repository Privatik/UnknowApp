package com.io.domain.util

sealed class Resource<out T>{

    class Success<T>(val data: T) : Resource<T>()

    class Error(val message: Exception) : Resource<Nothing>()

    object Loading : Resource<Nothing>()

}
