@file:OptIn(ExperimentalCoroutinesApi::class)

package com.io.data.token

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.io.data.di.DataServiceLocator
import com.io.data.encrypted.secureEdit
import com.io.data.encrypted.secureMap
import com.io.data.remote.Token
import com.io.data.remote.model.RefreshRequest
import com.io.data.remote.model.TokenResponse
import com.io.data.remote.requestAndConvertToResult
import com.io.data.storage.KeyForRefreshToken
import com.io.data.storage.KeyForUserId
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlin.coroutines.EmptyCoroutineContext

interface TokenManager<C> {

    suspend fun updateToken(client: C, oldToken: String?): String?
}

class JWTTokenManager(
    private val accessTokenProvider: TokenProvider,
    private val refreshTokenProvider: TokenProvider,
    private val dataStore: DataStore<Preferences>
) : TokenManager<HttpClient>{
    private var channel: SendChannel<Action>? = null
    private val mutex = Mutex()

    override suspend fun updateToken(client: HttpClient, oldToken: String?): String? {
        checkOnInit()

        val deferred = CompletableDeferred<String?>()
        channel?.send(Action.UpdateToken(client, oldToken, deferred)) ?: return null
        return deferred.await()
    }

    private suspend fun checkOnInit() = withContext(EmptyCoroutineContext){
        if (channel == null){
            mutex.tryLock{
                if (channel == null){
                    channel = actionActor()
                }
            }
        }
    }

    private sealed class Action{
        data class UpdateToken(
            val client: HttpClient,
            val oldToken: String?,
            val deferred: CompletableDeferred<String?>
        ): Action()
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun CoroutineScope.actionActor() = actor<Action>{
        for (action in channel) {
            when(action) {
                is Action.UpdateToken -> {
                    if ("Bearer ${accessTokenProvider.getToken()}" == action.oldToken){
                        accessTokenProvider.updateToken(null)

                        val token: Result<TokenResponse> = action.client.requestAndConvertToResult(
                            urlString = "${DataServiceLocator.baseApi}/api/refresh_token",
                            attributes = mapOf(AttributeKey<Token>("Token") to Token.REFRESH),
                            method = HttpMethod.Post
                        ){
                            body = RefreshRequest(
                                dataStore.data.map { it[KeyForUserId].orEmpty() }.distinctUntilChanged().first()
                            )
                        }

                        val updateToken = token.getOrNull() ?: kotlin.run {
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