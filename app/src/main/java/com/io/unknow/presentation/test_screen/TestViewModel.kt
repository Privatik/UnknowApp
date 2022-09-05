package com.io.unknow.presentation.test_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.io.data.repository.TestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TestViewModel(
    private val testRepository: TestRepository
): ViewModel() {
    private val _state = MutableStateFlow("")
    val state = _state.asStateFlow()

    init {
        testRepository.textFlow
            .onEach {
                _state.emit(it)
            }
            .launchIn(viewModelScope)
    }

    fun update(text: String) = viewModelScope.launch {
        testRepository.update(text)
    }
}