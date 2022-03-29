package com.io.unknow.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.io.data.internal.repository.LoginRepositoryImpl
import com.io.domain.usecase.AuthorizationUseCase
import javax.inject.Inject

class LoginViewModel(
): ViewModel() {
    private val useCase = AuthorizationUseCase(LoginRepositoryImpl())

    private val _emailText = mutableStateOf("")
    val emailText: State<String> = _emailText

    private val _passwordText = mutableStateOf("")
    val passwordText: State<String> = _passwordText

    private val _errorEmailText = mutableStateOf("")
    val erroeEmailText: State<String> = _errorEmailText

    private val _errorPasswordText = mutableStateOf("")
    val erroePasswordText: State<String> = _errorPasswordText

    fun setEmailText(email: String){
        _emailText.value = email
    }

    fun setPasswordText(password: String) {
        _passwordText.value = password
    }

    fun setErrorEmailText(error: String){
        _errorEmailText.value = error
    }

    fun setErrorPasswordText(error: String){
        _errorEmailText.value = error
    }
}