package com.io.data.token

import com.io.data.remote.ResponseBody
import com.io.data.remote.model.RefreshRequest
import com.io.data.remote.model.TokenResponse
import com.io.data.remote.requestAndConvertToResult
import com.io.data.storage.DataStorage
import com.io.data.storage.KeyForUserId
import com.io.data.storage.userIdKey
import io.ktor.client.*
import io.ktor.http.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.first
import kotlin.coroutines.CoroutineContext

interface TokenManager<C>{

    suspend fun updateTokens(accessToken: String?, refreshToken: String?)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?

    suspend fun updateToken(client: C, oldToken: String?): String?
}

class JWTTokenManager(
    private val coroutineScope: CoroutineScope,
    private val baseApi: String,
    private val accessTokenProvider: TokenProvider,
    private val refreshTokenProvider: TokenProvider,
    private val dataStore: DataStorage
) : TokenManager<HttpClient>{

    private var channel: SendChannel<Action> = actionActor()

    override suspend fun updateTokens(accessToken: String?, refreshToken: String?) {
        accessTokenProvider.updateToken(accessToken)
        refreshTokenProvider.updateToken(refreshToken)
    }

    override suspend fun getAccessToken(): String? {
        return accessTokenProvider.getToken()
    }

    override suspend fun getRefreshToken(): String? {
        return refreshTokenProvider.getToken()
    }

    override suspend fun updateToken(client: HttpClient, oldToken: String?): String? {
        val deferred = CompletableDeferred<String?>()
        channel.send(Action.UpdateToken(client, oldToken, deferred))
        return deferred.await()
    }

    private sealed class Action{
        data class UpdateToken(
            val client: HttpClient,
            val oldToken: String?,
            val deferred: CompletableDeferred<String?>
        ): Action()
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun actionActor() = coroutineScope.actor<Action>{
        for (action in channel) {
            when(action) {
                is Action.UpdateToken -> {
                    if ("Bearer ${accessTokenProvider.getToken()}" == action.oldToken){
                        accessTokenProvider.updateToken(null)

                        val token: Result<ResponseBody<TokenResponse>> = action.client.requestAndConvertToResult(
                            urlString = "${baseApi}/api/refresh_token",
                            method = HttpMethod.Post
                        ){
                            body = RefreshRequest(
                                dataStore.secureData(
                                    keyAlias = userIdKey,
                                    fetchValue = { it[KeyForUserId].orEmpty() },
                                    mapper = {it }
                                ).first()
                            )
                        }

                        val updateToken = token.getOrNull()?.message ?: kotlin.run {
                            action.deferred.complete(null)
                            return@actor
                        }

                        accessTokenProvider.updateToken(updateToken.accessToken)
                        refreshTokenProvider.updateToken(updateToken.refreshToken)

                        action.deferred.complete(updateToken.accessToken)
                    } else {
                        action.deferred.complete(accessTokenProvider.getToken())
                    }
                }
            }
        }
    }
}