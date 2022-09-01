package com.io.unknow.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import com.io.unknow.presentation.chat.ChatViewModel

fun factory(viewModel: ViewModel): ViewModelProvider.Factory{
    return object : ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return viewModel as T
        }
    }
}