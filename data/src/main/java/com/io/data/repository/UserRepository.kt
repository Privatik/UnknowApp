package com.io.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.io.data.di.DataServiceLocator
import com.io.data.encrypted.CryptoManager
import com.io.data.encrypted.secureEdit
import com.io.data.remote.UserApi
import com.io.data.remote.UserApiImpl
import com.io.data.storage.*
import com.io.data.storage.KeyForRefreshToken
import com.io.data.storage.KeyForUserId
import com.io.data.storage.KeyForUserName
import com.io.data.token.TokenManager
import com.io.domain.repository.UserRepository
import io.ktor.client.*

fun implUserRepository(
): UserRepository {
    val instance = DataServiceLocator.instance()
    return UserRepositoryImpl(
        instance.cryptoManager,
        instance.context.userPreferencesDataStore,
        UserApiImpl(instance.client),
        instance.jwtTokenManager
    )
}


internal class UserRepositoryImpl(
    private val cryptoManager: CryptoManager,
    private val dataStore: DataStore<Preferences>,
    private val userApi: UserApi,
    private val tokenManager: TokenManager<HttpClient>
):UserRepository{

    override suspend fun register(email: String, userName: String, password: String): Result<Boolean> {
        return userApi.register(email, userName, password)
            .onSuccess { response ->
                dataStore.edit { preference ->
                    preference[KeyForUserName] = response.message.user.userName
                }
                dataStore.secureEdit(cryptoManager, userIdKey, response.message.user.id) { preference, encryptedValue ->
                    preference[KeyForUserId] = encryptedValue
                }
                response.message.tokens.apply {
                    tokenManager.updateTokens(accessToken, refreshToken)
                }
            }
            .map { it.isSuccessful }
    }

    override suspend fun login(email: String, password: String): Result<Boolean>{
        return userApi.login(email, password)
            .onSuccess { response ->
                dataStore.edit { preference ->
                    preference[KeyForUserName] = response.message.user.userName
                }
                dataStore.secureEdit(cryptoManager, userIdKey, response.message.user.id) { preference, encryptedValue ->
                    preference[KeyForUserId] = encryptedValue
                }
                response.message.tokens.apply {
                    tokenManager.updateTokens(accessToken, refreshToken)
                }
            }.map {
                it.isSuccessful
            }
    }

    override suspend fun logout(): Result<Boolean> {
        return userApi.logout()
            .onSuccess { _ ->
                dataStore.edit { preference ->
                    preference.remove(KeyForUserName)
                    preference.remove(KeyForUserId)
                }
                tokenManager.updateTokens(null, null)
            }
            .map { it.isSuccessful }
    }

}