package com.io.data.repository

import com.io.data.di.DataServiceLocator
import com.io.data.remote.UserApi
import com.io.data.remote.implUserApi
import com.io.data.storage.DataStorage
import com.io.data.storage.KeyForUserId
import com.io.data.storage.KeyForUserName
import com.io.data.storage.userIdKey
import com.io.data.token.TokenManager
import com.io.domain.repository.UserRepository
import io.ktor.client.*
import kotlinx.coroutines.flow.Flow

fun implUserRepository(
): UserRepository {
    val instance = DataServiceLocator.instance()
    return UserRepositoryImpl(
        instance.dataStorage,
        implUserApi(),
        instance.jwtTokenManager
    )
}


internal class UserRepositoryImpl(
    private val dataStore: DataStorage,
    private val userApi: UserApi,
    private val tokenManager: TokenManager<HttpClient>
):UserRepository{
    override val userId: Flow<String>
        get() = dataStore.secureData(
            keyAlias = userIdKey,
            fetchValue = { preference ->
                preference[KeyForUserId].orEmpty()
            },
            mapper = { it }
        )

    override suspend fun register(email: String, userName: String, password: String): Result<Boolean> {
        return userApi.register(email, userName, password)
            .onSuccess { response ->
                dataStore.edit { preference ->
                    preference[KeyForUserName] = response.message.user.userName
                }
                dataStore.securityEdit(userIdKey, response.message.user.id) { preference, encryptedValue ->
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
                dataStore.securityEdit(userIdKey, response.message.user.id) { preference, encryptedValue ->
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
        dataStore.edit { preference ->
            preference.remove(KeyForUserName)
            preference.remove(KeyForUserId)
        }
        tokenManager.updateTokens(null, null)
        return Result.success(true)
    }

}