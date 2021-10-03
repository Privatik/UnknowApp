package com.io.unknow.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.io.unknow.R
import com.io.unknow.presentation.components.StandardTextField
import com.io.unknow.presentation.theme.SpaceMedium
import com.io.unknow.presentation.theme.SpaceSmall

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SpaceMedium),
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(id = R.string.logo)
        )
        Spacer(modifier = Modifier.height(SpaceMedium))
        StandardTextField(
            text = viewModel.emailText.value,
            hint = stringResource(R.string.hint_email),
            keyboardType = KeyboardType.Email,
            onValueChange = {
                viewModel.setEmailText(it)
            }
        )
        Spacer(modifier = Modifier.height(SpaceMedium))
        StandardTextField(
            text = viewModel.passwordText.value,
            hint = stringResource(R.string.hint_password),
            keyboardType = KeyboardType.Password,
            onValueChange = {
                viewModel.setPasswordText(it)
            }
        )
    }
}