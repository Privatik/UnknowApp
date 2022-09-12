package com.io.data.encrypted

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

suspend inline fun DataStore<Preferences>.secureEdit(
    cryptoManager: CryptoManager,
    keyAlias: String,
    value: String,
    crossinline editStore: (MutablePreferences, String) -> Unit
) {
    edit {
        val encryptedValue = cryptoManager.encryptData(keyAlias, value)
        editStore.invoke(it, encryptedValue)
    }
}

inline fun Flow<Preferences>.secureMap(
    cryptoManager: CryptoManager,
    keyAlias: String,
    crossinline fetchValue: (value: Preferences) -> String?
): Flow<String> {
    return map {
        val value = fetchValue(it)
        if (value.isNullOrBlank()) return@map ""

        val decryptedValue = cryptoManager.decryptData(
            keyAlias,
            value
        )
        decryptedValue
    }
}