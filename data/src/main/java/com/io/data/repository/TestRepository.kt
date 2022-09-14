package com.io.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.io.data.di.DataServiceLocator
import com.io.data.encrypted.CryptoManager
import com.io.data.encrypted.secureEdit
import com.io.data.encrypted.secureMap
import com.io.data.remote.TestApi
import com.io.data.remote.TestApiImpl
import com.io.data.remote.model.LogicResponse
import com.io.data.storage.KeyForRefreshToken
import com.io.data.storage.userPreferencesDataStore
import com.io.data.token.TokenManager
import io.ktor.client.*
import io.ktor.client.features.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext

interface TestRepository {
    val authInfo: Flow<String>

    suspend fun doAuth(
        email: String,
        password: String,
        nickName: String
    )
}

fun impl(context: Context): TestRepositoryImpl{
    val service = DataServiceLocator.instance(context)
    return TestRepositoryImpl(service.jwtTokenManager, context.userPreferencesDataStore, TestApiImpl(service.client, service.baseApi))
}

class TestRepositoryImpl(
    private val tokenManager: TokenManager<HttpClient>,
    private val dataStore: DataStore<Preferences>,
    private val api: TestApi,
): TestRepository{
    private val _authInfo = MutableSharedFlow<String>()

    override val authInfo: Flow<String> = _authInfo.asSharedFlow()

    override suspend fun doAuth(email: String, password: String, nickName: String) = withContext(EmptyCoroutineContext) {
        Log.d("Token","request ${Thread.currentThread()})")
        api.doRequest(email, password, nickName)
            .onSuccess {
                _authInfo.emit("Auth")
                it.message.tokens.apply { tokenManager.updateTokens(accessToken, refreshToken) }
            }
            .onFailure {
                Log.d("Ktor =>:",it.message.toString())
                _authInfo.emit("Error auth")
                return@withContext
            }

        delay(1000 * 30)
        while (isActive){
            api.checkValid()
                .onSuccess { response ->
                    _authInfo.emit("Reconnect auth")
                }
                .onFailure {
                    Log.d("Ktor =>:",it.toString())
                    _authInfo.emit("Error reconnect auth")
                    return@withContext
                }
            delay(1000 * 30)
        }
    }


}