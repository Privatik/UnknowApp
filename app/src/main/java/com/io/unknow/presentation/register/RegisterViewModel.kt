package com.io.unknow.presentation.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.io.unknow.domain.util.Sex
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(

): ViewModel() {

    private val _emailText = mutableStateOf("")
    val emailText: State<String> = _emailText

    private val _passwordText = mutableStateOf("")
    val passwordText: State<String> = _passwordText

    private val _errorEmailText = mutableStateOf("")
    val erroeEmailText: State<String> = _errorEmailText

    private val _errorPasswordText = mutableStateOf("")
    val erroePasswordText: State<String> = _errorPasswordText

    private val _sexRation = mutableStateOf(Sex.MEN)
    val sexRation: State<Sex> = _sexRation

    private val _birthDayText = mutableStateOf("")
    val birthDayText: State<String> = _birthDayText

    fun setEmailText(email: String){
        _emailText.value = email
    }

    fun setPasswordText(password: String) {
        _passwordText.value = password
    }

    fun setSexRation(sex: Sex){
        _sexRation.value = sex
    }

    fun setDatBirthDay(date:String){
        _birthDayText.value = date
    }

    fun setErrorEmailText(error: String){
        _errorEmailText.value = error
    }

    fun setErrorPasswordText(error: String){
        _errorEmailText.value = error
    }
}