package com.io.data.storage

import android.content.Context
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.google.protobuf.InvalidProtocolBufferException
import com.io.data.entify.AuthToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.io.InputStream
import java.io.OutputStream

interface PrefAuth {

    fun saveAuthToken(
        auth: AuthToken
    )

    fun getAuthToken(): AuthToken
}

internal class PrefAuthImpl(
   context: Context
){

    private val datastore =  DataStoreFactory.create(
        serializer = AuthTokenSerializer,
        produceFile = { context.dataStoreFile("auth_token") }
    )


}

internal object AuthTokenSerializer: Serializer<AuthToken>{

    override suspend fun readFrom(input: InputStream): AuthToken {
        try {

        }catch (e: InvalidProtocolBufferException){

        }
        return AuthToken.empty()
    }

    override suspend fun writeTo(t: AuthToken, output: OutputStream) {

    }

    override val defaultValue: AuthToken
        get() = AuthToken.empty()

}