package com.io.unknow.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.domain.usecase.RegisterUseCase
import com.io.unknow.presentation.util.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class RegisterState(
    val email: String = "",
    val userName: String = "",
    val password: String = "",
    val isLoading: Boolean = false
)

sealed class RegisterEffect{

    data class OnPopBack(val screen: String? = null): RegisterEffect()
    data class OpenNextScreen(val screen: String): RegisterEffect()

}

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase = RegisterUseCase()
): ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RegisterEffect>()
    val effect: SharedFlow<RegisterEffect> = _effect.asSharedFlow()

    fun setEmail(email: String) = viewModelScope.launch {
        _state.emit(_state.value.copy(email = email))
    }

    fun setUserName(userName: String) = viewModelScope.launch {
        _state.emit(_state.value.copy(userName = userName))
    }

    fun setPassword(password: String) = viewModelScope.launch {
        _state.emit(_state.value.copy(password = password))
    }

    fun actionOnBack() = viewModelScope.launch{
        _effect.emit(RegisterEffect.OnPopBack())
    }

    fun actionRegister() = viewModelScope.launch(Dispatchers.IO) {
        _state.emit(_state.value.copy(isLoading = true))

        registerUseCase.invoke(state.value.userName, state.value.password)
            .onSuccess {
                _effect.emit(RegisterEffect.OpenNextScreen(Screen.ChatScreen.route))
            }
            .onFailure {}

        _state.emit(_state.value.copy(isLoading = false))
    }
}