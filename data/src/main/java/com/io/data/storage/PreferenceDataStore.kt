package com.io.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

private const val PreferencesName = "TestDevice"
internal val KeyForRefreshToken = stringPreferencesKey("refresh-token")

val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = PreferencesName
)



