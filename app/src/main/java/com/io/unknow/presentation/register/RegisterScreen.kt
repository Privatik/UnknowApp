package com.io.unknow.presentation.register

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
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
fun RegisterScreen(
    onBack:(String?) -> Unit,
    openNextScreen:(String) -> Unit,
    viewModel: RegisterViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
){
    val state by viewModel.state.collectAsState()
    
    LaunchedEffect(Unit){
        viewModel.effect
            .onEach {
                when(it){
                    is RegisterEffect.OnPopBack -> onBack(it.screen)
                    is RegisterEffect.OpenNextScreen -> openNextScreen(it.screen)
                }
            }
            .launchIn(this)
    }
    

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .navigationBarsWithImePadding()
            .fillMaxSize()
            .padding(SpaceMedium),
        verticalArrangement = Arrangement.Center,
    ) {
        StandardTextField(
            text = state.userName,
            hint = stringResource(R.string.hint_username),
            keyboardType = KeyboardType.Text,
            onValueChange = {
                viewModel.setUserName(it)
            }
        )
        Spacer(modifier = Modifier.height(SpaceMedium))
        StandardTextField(
            text = state.password,
            hint = stringResource(R.string.hint_password),
            keyboardType = KeyboardType.Password,
            onValueChange = {
                viewModel.setPassword(it)
            },
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
                onClick = { onBack(null) },
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onPrimary
                )
            ) {
                Text(
                    text = stringResource(id = R.string.back),
                    color = Color.White,
                    modifier = Modifier.padding(SpaceSmall)
                )
            }
            Button(
                onClick = {},
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onPrimary
                ),
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    color = Color.White,
                    modifier = Modifier.padding(SpaceSmall)
                )
            }
        }
    }
}