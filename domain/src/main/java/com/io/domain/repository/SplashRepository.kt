package com.io.domain.repository

interface SplashRepository {

    suspend fun isAuth(): Boolean
}