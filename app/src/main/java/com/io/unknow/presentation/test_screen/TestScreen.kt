package com.io.unknow.presentation.test_screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TestScreen(
    viewModel: TestViewModel
){
    val state by viewModel.state.collectAsState()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = state.stateDescription)
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = state.email,
            placeholder = {
                Text(text = "Email")
            },
            onValueChange = viewModel::updateEmail
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = state.password,
            placeholder = {
                Text(text = "Password")
            },
            onValueChange = viewModel::updatePassword
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = state.nickname,
            placeholder = {
                Text(text = "NickName")
            },
            onValueChange = viewModel::updateNickName
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = viewModel::doAuth) {
            Text(text = "Auth")
        }
    }
}