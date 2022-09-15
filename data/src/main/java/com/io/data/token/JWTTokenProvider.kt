package com.io.data.token

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.io.data.encrypted.CryptoManager
import com.io.data.encrypted.secureEdit
import com.io.data.encrypted.secureMap
import com.io.data.storage.KeyForRefreshToken
import com.io.data.storage.refreshKey
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class JWTAccessTokenProvider(): TokenProvider{
    override val coroutineContext: CoroutineContext = Dispatchers.Default
    private val channel: SendChannel<Action> = actionActor()

    override suspend fun updateToken(token: String?) {
        channel.send(Action.TokenSet(token))
    }

    override suspend fun getToken(): String  {
        var answer: String? = null
        while (answer == null) {
            val deferred = CompletableDeferred<String?>()
            channel.send(Action.TokenGet(deferred))
            answer = deferred.await()
        }
        return answer
    }

    private sealed class Action(){
        data class TokenGet(val deferred: CompletableDeferred<String?>): Action()
        data class TokenSet(val body: String?): Action()
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun actionActor() = actor<Action>{
        var token: String? = null

        for (action in channel) {
            when(action) {
                is Action.TokenGet -> {
                    action.deferred.complete(token)
                }
                is Action.TokenSet -> {
                    token = action.body
                }
            }
        }
    }
}

class JWTRefreshTokenProvider(
    private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager
): TokenProvider {

    override val coroutineContext: CoroutineContext = Dispatchers.Default
    private var channel: SendChannel<Action> = actionActor()

    override suspend fun updateToken(token: String?) {
        channel.send(Action.TokenSet(token))
    }

    override suspend fun getToken(): String?  {
        val deferred = CompletableDeferred<String?>()
        channel.send(Action.TokenGet(deferred)) ?: return null
        return deferred.await()
    }

    private sealed class Action(){
        data class TokenGet(val deferred: CompletableDeferred<String?>): Action()
        data class TokenSet(val body: String?): Action()
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun actionActor() = actor<Action>{
        var isUpdate = false

        for (token in channel) {
            when(token) {
                is Action.TokenGet -> {
                    if (isUpdate) {
                        token.deferred.complete(null)
                        return@actor
                    }
                    val refreshToken = dataStore.data
                        .secureMap(cryptoManager, refreshKey){ preference ->
                            preference[KeyForRefreshToken].orEmpty()
                        }
                        .first()

                    if (refreshToken.isNotBlank()){
                        token.deferred.complete(refreshToken)
                    }
                    isUpdate = true
                }
                is Action.TokenSet -> {
                    dataStore.secureEdit(cryptoManager, refreshKey, token.body.orEmpty()) { preference, encryptedValue ->
                        preference[KeyForRefreshToken] = encryptedValue
                    }
                    isUpdate = false
                }
            }
        }
    }

}