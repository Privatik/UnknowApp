package com.io.data.encrypted

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

const val bytesToStringSeparator = "_"
suspend inline fun DataStore<Preferences>.secureEdit(
    value: String,
    crossinline editStore: (MutablePreferences, String) -> Unit
) {
    edit {
        println("Encry start code")
        val encryptedValue = encryptData("AndroidKey", value)
        editStore.invoke(it, encryptedValue.joinToString(bytesToStringSeparator))
        println("Encry end code")
    }
}

inline fun Flow<Preferences>.secureMap(
    crossinline fetchValue: (value: Preferences) -> String?
): Flow<String> {
    return map {
        val value = fetchValue(it)
        if (value.isNullOrBlank()) return@map ""


        val decryptedValue = decryptData(
            "AndroidKey",
            value
                .split(bytesToStringSeparator)
                .map { listStr -> listStr.toByte() }
                .toByteArray()
        )
        decryptedValue
    }
}