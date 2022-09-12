package com.io.unknow.presentation.test_screen

import android.provider.ContactsContract
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.data.repository.TestRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.FileDescriptor

@Immutable
data class TestState(
    val email: String = "",
    val password: String = "",
    val nickname: String = "" ,
    val stateDescription: String = "Not Auth"
)

class TestViewModel(
    private val testRepository: TestRepository
): ViewModel() {
    private val _state = MutableStateFlow(TestState())
    val state = _state.asStateFlow()

    init {
        testRepository.authInfo
            .onEach {
                _state.emit((_state.value.copy(stateDescription = it)))
            }
            .launchIn(viewModelScope)
    }

    fun updateEmail(text: String) = viewModelScope.launch {
        _state.emit(_state.value.copy(email = text))
    }

    fun updatePassword(text: String) = viewModelScope.launch{
        _state.emit(_state.value.copy(password = text))
    }

    fun updateNickName(text: String) = viewModelScope.launch {
        _state.emit(_state.value.copy(nickname = text))
    }

    fun doAuth() = viewModelScope.launch(Dispatchers.IO) {
        testRepository.doAuth(
            _state.value.email,
            _state.value.password,
            _state.value.nickname
        )
    }
}