package com.io.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.io.data.encrypted.secureEdit
import com.io.data.encrypted.secureMap
import com.io.data.storage.KeyForRefreshToken
import com.io.data.storage.userPreferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

interface TestRepository {
    val textFlow: Flow<String>

    suspend fun update(text: String)
}

fun impl(context: Context): TestRepositoryImpl{
    return TestRepositoryImpl(context.userPreferencesDataStore)
}

class TestRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): TestRepository{
    override val textFlow: Flow<String> = dataStore
        .data
        .secureMap { it[KeyForRefreshToken].orEmpty() }
        .distinctUntilChanged()

    override suspend fun update(text: String) {
        dataStore.secureEdit(
            value = text,
            editStore = { preference, encryptedValue ->
                preference[KeyForRefreshToken] = encryptedValue
            }
        )
    }

}