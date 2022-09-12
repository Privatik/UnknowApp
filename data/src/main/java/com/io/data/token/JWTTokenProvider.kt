package com.io.data.token

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.io.data.encrypted.CryptoManager
import com.io.data.encrypted.secureEdit
import com.io.data.encrypted.secureMap
import com.io.data.storage.KeyForRefreshToken
import io.ktor.http.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex

class JWTAccessTokenProvider(): TokenProvider {
    @Volatile
    private var token: String? = null
    private val mutex = Mutex()

    override suspend fun updateToken(token: String?) {
        this.token = token
        token?.let { mutex.unlock() }
    }

    override suspend fun addTokenInHeader(headers: HeadersBuilder) {
        if (token == null) mutex.lock()
        token?.let { accessToken ->
            headers.append("Authorization","Bearer $accessToken")
        }
    }
}

class JWTRefreshTokenProvider(
    private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager
): TokenProvider {
    private val mutex = Mutex()

    override suspend fun updateToken(token: String?) {

        dataStore.secureEdit(cryptoManager, "Refresh", token!!) { preference, encryptedValue ->
                preference[KeyForRefreshToken] = encryptedValue
            }
        mutex.unlock()
    }

    override suspend fun addTokenInHeader(headers: HeadersBuilder) {
        val refreshToken = dataStore.data
            .secureMap(cryptoManager,"Refresh"){ preference ->
                preference[KeyForRefreshToken].orEmpty()
            }
            .first()

        headers.append("Authorization","Bearer $refreshToken")
    }
}