package com.io.unknow.presentation.list_of_dialogs

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class ListOfDialogViewModel @Inject constructor(

): ViewModel() {
    private val _searchText = mutableStateOf("")
    val searchText: State<String> = _searchText

    fun setSearchText(search: String){
        _searchText.value = search
    }
}