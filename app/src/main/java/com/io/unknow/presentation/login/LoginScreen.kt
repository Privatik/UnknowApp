package com.io.unknow.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.io.unknow.R
import com.io.unknow.presentation.components.StandardTextField
import com.io.unknow.presentation.ui.theme.SpaceLarge
import com.io.unknow.presentation.ui.theme.SpaceMedium
import com.io.unknow.presentation.ui.theme.SpaceSmall
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun LoginScreen(
    openNextScreen:(String) -> Unit,
    viewModel: LoginViewModel = viewModel()
){
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit){
        viewModel.effect
            .onEach { effect -> effect.navigate?.let(openNextScreen) }
            .launchIn(this)
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsWithImePadding()
            .fillMaxSize()
            .padding(SpaceMedium)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(id = R.string.logo)
        )
        Spacer(modifier = Modifier.height(SpaceMedium))
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            StandardTextField(
                text = state.email,
                hint = stringResource(R.string.hint_email),
                keyboardType = KeyboardType.Email,
                onValueChange = {
                    viewModel.setEmailText(it)
                },
                focusedIndicatorColor = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(SpaceMedium))
            StandardTextField(
                text = state.password,
                hint = stringResource(R.string.hint_password),
                keyboardType = KeyboardType.Password,
                onValueChange = {
                    viewModel.setPasswordText(it)
                },
                focusedIndicatorColor = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(SpaceLarge))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpaceSmall),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = viewModel::actionLogin,
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onPrimary
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.sing_up),
                        color = Color.White,
                        modifier = Modifier.padding(SpaceSmall)
                    )
                }

                Button(
                    onClick = viewModel::actionRegister,
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onPrimary
                    ),
                ) {
                    Text(
                        text = stringResource(id = R.string.sing_in),
                        color = Color.White,
                        modifier = Modifier.padding(SpaceSmall)
                    )
                }
            }
        }
    }
}