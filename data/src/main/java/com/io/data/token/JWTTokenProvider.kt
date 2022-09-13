package com.io.data.token

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.io.data.encrypted.CryptoManager
import com.io.data.encrypted.secureEdit
import com.io.data.encrypted.secureMap
import com.io.data.storage.KeyForRefreshToken
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext

class JWTAccessTokenProvider(): TokenProvider {
    private val mutex = Mutex()
    private var channel: SendChannel<Action>? = null

    override suspend fun updateToken(token: String?): Unit = withContext(EmptyCoroutineContext) {
        checkOnInit()
        channel?.send(Action.TokenSet(token))
    }

    override suspend fun getToken(): String?  {
        var answer: String? = null
        while (answer == null) {
            val deferred = CompletableDeferred<String?>()
            channel?.send(Action.TokenGet(deferred)) ?: return null
            answer = deferred.await()
        }
        return answer
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

    private sealed class Action(){
        data class TokenGet(val deferred: CompletableDeferred<String?>): Action()
        data class TokenSet(val body: String?): Action()
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun CoroutineScope.actionActor() = actor<Action>{
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
    private var channel: SendChannel<Action>? = null
    private val mutex = Mutex()

    override suspend fun updateToken(token: String?): Unit = withContext(EmptyCoroutineContext) {
        checkOnInit()
        channel?.send(Action.TokenSet(token))
    }

    override suspend fun getToken(): String?  {
        val deferred = CompletableDeferred<String?>()
        channel?.send(Action.TokenGet(deferred)) ?: return null
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

    private sealed class Action(){
        data class TokenGet(val deferred: CompletableDeferred<String?>): Action()
        data class TokenSet(val body: String?): Action()
    }

    @OptIn(ObsoleteCoroutinesApi::class)
    private fun CoroutineScope.actionActor() = actor<Action>{
        var isUpdate = false

        for (token in channel) {
            when(token) {
                is Action.TokenGet -> {
                    if (isUpdate) {
                        token.deferred.complete(null)
                        return@actor
                    }
                    val refreshToken = dataStore.data
                        .secureMap(cryptoManager,"Refresh"){ preference ->
                            preference[KeyForRefreshToken].orEmpty()
                        }
                        .first()

                    if (refreshToken.isNotBlank()){
                        token.deferred.complete(refreshToken)
                    }
                    isUpdate = true
                }
                is Action.TokenSet -> {
                    dataStore.secureEdit(cryptoManager, "Refresh", token.body.orEmpty()) { preference, encryptedValue ->
                        preference[KeyForRefreshToken] = encryptedValue
                    }
                    isUpdate = false
                }
            }
        }
    }

}