package com.io.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.io.data.di.DataServiceLocator
import com.io.data.storage.KeyForRefreshToken
import com.io.data.storage.refreshKey
import com.io.domain.repository.SplashRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

fun implSplashRepository(): SplashRepository{
    val instance = DataServiceLocator.instance()
    return SplashRepositoryImpl(instance.dataStore)
}

class SplashRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): SplashRepository {

    override suspend fun isAuth(): Boolean {
        return !dataStore.data.map { it[KeyForRefreshToken] }.first().isNullOrBlank()
    }
}