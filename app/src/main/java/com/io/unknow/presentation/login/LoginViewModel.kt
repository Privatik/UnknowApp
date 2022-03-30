package com.io.unknow.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.data.internal.repository.LoginRepositoryImpl
import com.io.domain.usecase.AuthorizationUseCase
import com.io.domain.util.Resource
import com.io.unknow.presentation.util.Screen
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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

class LoginViewModel: ViewModel() {
    private val useCase = AuthorizationUseCase(LoginRepositoryImpl())

    private val _state = mutableStateOf(LoginState())
    val state: State<LoginState> = _state

    private val _effect = mutableStateOf(LoginEffect())
    val effect: State<LoginEffect> = _effect

    var loginJob: Job? = null

    fun setEmailText(email: String){
        _state.value = _state.value.copy(email = email)
    }

    fun setPasswordText(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun setErrorEmailText(error: String){
        _state.value = _state.value.copy(errorEmail = error)
    }

    fun setErrorPasswordText(error: String){
        _state.value = _state.value.copy(errorPassword = error)
    }

    fun login(){
        loginJob?.cancel()
        loginJob = useCase(email = state.value.email, password = state.value.password)
            .onEach { result ->
                when (result){
                    is Resource.Error -> {
                        _state.value = _state.value.copy(isLoading = false)
                        _effect.value = _effect.value.copy(
                            navigate = null,
                            errorMessage = result.message.message
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = _state.value.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        _state.value = _state.value.copy(
                            isLoading = false
                        )
                        _effect.value = _effect.value.copy(
                            navigate = Screen.ChatScreen.route,
                            errorMessage = null
                        )
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}