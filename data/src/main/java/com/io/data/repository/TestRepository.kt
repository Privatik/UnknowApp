package com.io.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.io.data.di.DataServiceLocator
import com.io.data.encrypted.secureEdit
import com.io.data.encrypted.secureMap
import com.io.data.remote.TestApi
import com.io.data.remote.TestApiImpl
import com.io.data.storage.KeyForRefreshToken
import com.io.data.storage.userPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

interface TestRepository {
    val textFlow: Flow<String>

    suspend fun update(text: String)

    suspend fun doRequest()
}

fun impl(context: Context): TestRepositoryImpl{
    return TestRepositoryImpl(context.userPreferencesDataStore, TestApiImpl(DataServiceLocator.client))
}

class TestRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val api: TestApi,
): TestRepository{
    override val textFlow: Flow<String> = dataStore
        .data
        .secureMap("AndroidKey") { it[KeyForRefreshToken].orEmpty() }
        .distinctUntilChanged()

    override suspend fun update(text: String) {
        dataStore.secureEdit(
            keyAlias = "AndroidKey",
            value = text,
            editStore = { preference, encryptedValue ->
                preference[KeyForRefreshToken] = encryptedValue
            }
        )
    }

    override suspend fun doRequest() {
        api.doRequest()
    }

}