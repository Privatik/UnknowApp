package com.io.unknow.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: Author
): ViewModel() {

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