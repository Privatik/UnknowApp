package com.io.data.token

import android.util.Log
import com.io.data.storage.DataStorage
import com.io.data.storage.KeyForRefreshToken
import com.io.data.storage.refreshKey
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.first
import kotlin.coroutines.CoroutineContext

class JWTAccessTokenProvider(
    private val coroutineScope: CoroutineScope
): TokenProvider{
    private val channel: SendChannel<Action> = actionActor()

    override suspend fun updateToken(token: String?) {
        channel.send(Action.TokenSet(token))
    }

    override suspend fun getToken(): String?  {
        val deferred = CompletableDeferred<String?>()
        channel.send(Action.TokenGet(deferred))
        return deferred.await()
    }

    private sealed class Action(){
        data class TokenGet(val deferred: CompletableDeferred<String?>): Action()
        data class TokenSet(val body: String?): Action()
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun actionActor() = coroutineScope.actor<Action>{
        var token: String? = null

        for (action in channel) {
            when(action) {
                is Action.TokenGet -> {
                    Log.d("Token","access Get")
                    action.deferred.complete(token)
                }
                is Action.TokenSet -> {
                    Log.d("Token","access Set")
                    token = action.body
                }
            }
        }
    }
}

class JWTRefreshTokenProvider(
    private val coroutineScope: CoroutineScope,
    private val dataStore: DataStorage
): TokenProvider {
    private var channel: SendChannel<Action> = actionActor()

    override suspend fun updateToken(token: String?) {
        channel.send(Action.TokenSet(token))
    }

    override suspend fun getToken(): String?  {
        val deferred = CompletableDeferred<String?>()
        channel.send(Action.TokenGet(deferred))
        return deferred.await()
    }

    private sealed class Action(){
        data class TokenGet(val deferred: CompletableDeferred<String?>): Action()
        data class TokenSet(val body: String?): Action()
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun actionActor() = coroutineScope.actor<Action>{
        var isUpdate = false

        for (token in channel) {
            when(token) {
                is Action.TokenGet -> {
                    Log.d("Token","refresh Get")
                    if (isUpdate) {
                        token.deferred.complete(null)
                        return@actor
                    }
                    val refreshToken = dataStore
                        .secureData(
                            keyAlias = refreshKey,
                            fetchValue = { preference ->
                                preference[KeyForRefreshToken].orEmpty()
                            },
                            mapper = { it }
                        )
                        .first()

                    if (refreshToken.isNotBlank()){
                        token.deferred.complete(refreshToken)
                    }
                    isUpdate = true
                }
                is Action.TokenSet -> {
                    Log.d("Token","refresh Set")
                    dataStore.securityEdit(refreshKey, token.body.orEmpty()) { preference, encryptedValue ->
                        preference[KeyForRefreshToken] = encryptedValue
                    }
                    isUpdate = false
                }
            }
        }
    }

}