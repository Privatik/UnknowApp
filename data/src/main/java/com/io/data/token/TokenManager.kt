package com.io.data.token

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.io.data.encrypted.CryptoManager
import com.io.data.encrypted.secureMap
import com.io.data.remote.ResponseBody
import com.io.data.remote.model.RefreshRequest
import com.io.data.remote.model.TokenResponse
import com.io.data.remote.requestAndConvertToResult
import com.io.data.storage.KeyForUserId
import com.io.data.storage.refreshKey
import com.io.data.storage.userIdKey
import io.ktor.client.*
import io.ktor.http.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlin.coroutines.CoroutineContext

interface TokenManager<C> : CoroutineScope{

    suspend fun updateTokens(accessToken: String?, refreshToken: String?)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?

    suspend fun updateToken(client: C, oldToken: String?): String?
}

class JWTTokenManager(
    private val baseApi: String,
    private val accessTokenProvider: TokenProvider,
    private val refreshTokenProvider: TokenProvider,
    private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager
) : TokenManager<HttpClient>{

    override val coroutineContext: CoroutineContext = Dispatchers.Default

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
    private fun actionActor() = actor<Action>{
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
                                dataStore.data.secureMap(cryptoManager, userIdKey) { it[KeyForUserId].orEmpty() }.distinctUntilChanged().first()
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