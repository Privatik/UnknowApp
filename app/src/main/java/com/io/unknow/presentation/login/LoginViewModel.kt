package com.io.unknow.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.domain.usecase.LoginUseCase
import com.io.unknow.presentation.util.Screen
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class LoginEffect(
    val navigate: String? = null,
    val errorMessage: String? = null
)

data class LoginState(
    val email: String = "",
    val password: String = "",
    val errorEmail: String = "",
    val errorPassword: String = "",
    val isLoading: Boolean = false
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase = LoginUseCase()
): ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEffect>()
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun setEmailText(email: String) = viewModelScope.launch {
        _state.emit(_state.value.copy(email = email))
    }

    fun setPasswordText(password: String) = viewModelScope.launch {
        _state.emit(_state.value.copy(password = password))
    }

    fun actionLogin() = viewModelScope.launch {
        _state.emit(_state.value.copy(isLoading = true))

        loginUseCase(userName = state.value.email, password = state.value.password)
            .onSuccess {
                _effect.emit(LoginEffect(navigate = Screen.ChatScreen.route))
            }
            .onFailure {
                _state.emit(_state.value.copy(errorEmail = it.message!!, errorPassword = it.message!!))
            }


        _state.emit(_state.value.copy(isLoading = false))
    }

    fun actionRegister() = viewModelScope.launch {
        _effect.emit(LoginEffect(navigate = Screen.RegisterScreen.route))
    }
}