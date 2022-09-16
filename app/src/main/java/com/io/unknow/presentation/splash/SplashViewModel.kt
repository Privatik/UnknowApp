package com.io.unknow.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.data.repository.SplashRepositoryImpl
import com.io.data.repository.implSplashRepository
import com.io.domain.repository.SplashRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val splashRepository: SplashRepository = implSplashRepository()
): ViewModel() {
    private val _isAuth = MutableStateFlow<Boolean?>(null)
    val isAuth = _isAuth.asStateFlow()

    fun isAuth() = viewModelScope.launch {
        _isAuth.emit(splashRepository.isAuth())
    }
}