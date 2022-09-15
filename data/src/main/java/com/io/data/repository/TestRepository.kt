package com.io.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.io.data.di.DataServiceLocator
import com.io.data.remote.TestApi
import com.io.data.remote.TestApiImpl
import com.io.data.storage.KeyForUserId
import com.io.data.storage.userPreferencesDataStore
import com.io.data.token.TokenManager
import io.ktor.client.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
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
        api.doRequest(email, password, nickName)
            .onSuccess {
                _authInfo.emit("Auth")
                it.message.tokens.apply { tokenManager.updateTokens(accessToken, refreshToken) }
                dataStore.edit { preference -> preference[KeyForUserId] = it.message.user.id }
            }
            .onFailure {
                _authInfo.emit("Error auth")
                return@withContext
            }


        repeat(7){ index ->
            launch {
                delay(1000 * 30)
                while (isActive){
                    api.checkValid()
                        .onSuccess { response ->
                            Log.d("Client","success $index")
                        }
                        .onFailure {
                            Log.d("Client","failure $index")
                            return@launch
                        }
                    delay(1000 * 30)
                }
            }
        }
    }


}