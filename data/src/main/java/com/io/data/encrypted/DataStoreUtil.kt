package com.io.data.encrypted

import android.util.Base64
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val bytesToStringSeparator = "|"
suspend inline fun DataStore<Preferences>.secureEdit(
    keyAlias: String,
    keyIv: String,
    value: String,
    crossinline editStore: (MutablePreferences, String) -> Unit
) {
    edit {
        val encryptedValue = encryptData(keyAlias, value)
        it[stringPreferencesKey(keyIv)] = Base64.encodeToString(encryptedValue.iv, Base64.DEFAULT)
        editStore.invoke(it, encryptedValue.encryptData.joinToString(bytesToStringSeparator))
    }
}

inline fun Flow<Preferences>.secureMap(
    keyAlias: String,
    keyIv: String,
    crossinline fetchValue: (value: Preferences) -> String?
): Flow<String> {
    return map {
        val value = fetchValue(it)
        if (value.isNullOrBlank()) return@map ""

        val decryptedValue = decryptData(
            keyAlias,
            value
                .split(bytesToStringSeparator)
                .map { listStr -> listStr.toByte() }
                .toByteArray(),
            Base64.decode(it[stringPreferencesKey(keyIv)]!!, Base64.DEFAULT)
        )
        decryptedValue
    }
}