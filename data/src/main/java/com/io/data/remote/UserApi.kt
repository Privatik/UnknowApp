package com.io.data.remote

import com.io.data.di.DataServiceLocator
import com.io.data.remote.model.LoginResponse
import com.io.data.remote.model.LoginRequest
import com.io.data.remote.model.UserResponse
import io.ktor.client.*
import io.ktor.client.request.forms.*
import io.ktor.http.*

fun implUserApi(): UserApi{
    val instance = DataServiceLocator.instance()
    return UserApiImpl(instance.client)
}

interface UserApi {

    suspend fun register(
        email: String,
        userName: String,
        password: String
    ): Result<ResponseBody<LoginResponse>>

    suspend fun login(
        email: String,
        password: String
    ): Result<ResponseBody<LoginResponse>>

    suspend fun logout(): Result<LogoutResponse>
}

internal class UserApiImpl(
    private val client: HttpClient
): UserApi{
    private val instance = DataServiceLocator.instance()

    override suspend fun register(
        email: String,
        userName: String,
        password: String
    ): Result<ResponseBody<LoginResponse>> = client.requestAndConvertToResult(
        urlString = "${instance.baseApi}/api/user/create",
        method = HttpMethod.Post
    ){
        val formParameters = formData {
            append("userName", userName)
            append("email", email)
            append("password", password)
        }

        body = MultiPartFormDataContent(formParameters)
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<ResponseBody<LoginResponse>> = client.requestAndConvertToResult(
        urlString = "${instance.baseApi}/api/login",
        method = HttpMethod.Post
    ) {
        body = LoginRequest(email, password)
    }

    override suspend fun logout(): Result<LogoutResponse> = client.requestAndConvertToResult(
        urlString = "${instance.baseApi}/api/logout",
        method = HttpMethod.Get
    )

}