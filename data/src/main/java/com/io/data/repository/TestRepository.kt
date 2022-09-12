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
    return TestRepositoryImpl(context.userPreferencesDataStore, TestApiImpl(DataServiceLocator.client))
}

class TestRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val api: TestApi,
): TestRepository{
    private val cryptoManager = CryptoManager()
    private val _authInfo = MutableSharedFlow<String>()

    override val authInfo: Flow<String> = _authInfo.asSharedFlow()

    override suspend fun doAuth(email: String, password: String, nickName: String) = withContext(EmptyCoroutineContext) {
        var loginResponse: LogicResponse? = null
        api.doRequest(email, password, nickName)
            .onSuccess {
                _authInfo.emit("Auth")
                loginResponse = it.message
            }
            .onFailure {
                Log.d("Ktor =>:",it.message.toString())
                _authInfo.emit("Error auth")
                return@withContext
            }

        delay(1000 * 30)
        while (isActive){
            api.checkValid(loginResponse?.tokens?.accessToken ?: "")
                .onSuccess { response ->
                    _authInfo.emit("Reconnect auth")
                }
                .onFailure {
                    if (it is ClientRequestException){
                        if (it.response.status.value in 300..500){
                            _authInfo.emit("try reconnect auth")
                            delay(1000 * 30)
                            api.refresh(
                                loginResponse?.user?.id ?: "",
                                loginResponse?.tokens?.refreshToken ?: ""
                            ).onSuccess {
                                loginResponse = loginResponse?.copy(tokens = it.message)
                            }.onFailure {
                                Log.d("Ktor =>:",it.message.toString())
                                _authInfo.emit("Error when reconnect auth")
                                return@withContext
                            }
                        }
                    } else {
                        Log.d("Ktor =>:",it.toString())
                        _authInfo.emit("Error reconnect auth")
                        return@withContext
                    }
                }
            delay(1000 * 30)
        }
    }


}