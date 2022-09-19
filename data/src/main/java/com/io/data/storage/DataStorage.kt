package com.io.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.io.data.encrypted.CryptoManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface DataStorage{
    val data: Flow<Preferences>

    fun <T: Any> secureData(
        keyAlias: String,
        fetchValue: (value: Preferences) -> String?,
        mapper: (String) -> T
    ): Flow<T>

    suspend fun edit(transform: suspend (MutablePreferences) -> Unit): Preferences

    suspend fun securityEdit(
        keyAlias: String,
        value: String,
        editStorage: suspend (MutablePreferences, String) -> Unit
    ): Preferences
}

class DataStorageImpl(
    private val dataStore: DataStore<Preferences>,
    private val cryptoManager: CryptoManager,
): DataStorage {
    override val data: Flow<Preferences> = dataStore.data

    override fun <T: Any> secureData(
        keyAlias: String,
        fetchValue: (value: Preferences) -> String?,
        mapper: (String) -> T
    ): Flow<T>{
        return data.map {
            val value = fetchValue(it)
            if (value.isNullOrBlank()) return@map mapper("")

            val decryptedValue = cryptoManager.decryptData(
                keyAlias,
                value
            )

            mapper(decryptedValue)
        }
    }

    override suspend fun edit(transform: suspend (MutablePreferences) -> Unit): Preferences{
        return dataStore.edit(transform)
    }

    override suspend fun securityEdit(
        keyAlias: String,
        value: String,
        editStorage: suspend (MutablePreferences, String) -> Unit
    ): Preferences{
        return dataStore.edit {
            if (value.isBlank()) {
                editStorage.invoke(it, "")
            } else {
                val encryptedValue = cryptoManager.encryptData(keyAlias, value)
                editStorage.invoke(it, encryptedValue)
            }
        }
    }
}