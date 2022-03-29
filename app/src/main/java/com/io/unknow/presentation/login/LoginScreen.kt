package com.io.unknow.presentation.login

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.io.unknow.R
import com.io.unknow.presentation.components.StandardTextField
import com.io.unknow.presentation.ui.theme.SpaceLarge
import com.io.unknow.presentation.ui.theme.SpaceMedium
import com.io.unknow.presentation.ui.theme.SpaceSmall
import com.io.unknow.presentation.util.Screen

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
){
    val configuration = LocalConfiguration.current

    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
        LoginScreenLandscape(
            navController = navController,
            viewModel = viewModel)
    } else {
        LoginScreenPortrait(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun LoginScreenPortrait(
    navController: NavController,
    viewModel: LoginViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(SpaceMedium)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(id = R.string.logo)
        )
        Spacer(modifier = Modifier.height(SpaceMedium))
        LoginColumn(
            navController = navController,
            viewModel = viewModel)
    }
}

@Composable
fun LoginScreenLandscape(
    navController: NavController,
    viewModel: LoginViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(SpaceMedium),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(id = R.string.logo),
            //modifier = Modifier.offset(y = SpaceSmall)
        )
        LoginColumn(
            navController = navController,
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpaceMedium)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun LoginColumn(
    navController: NavController,
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center
    ) {
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
        Spacer(modifier = Modifier.height(SpaceLarge))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(SpaceSmall),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    navController.navigate(Screen.RegisterScreen.route)
                },
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
                onClick = {
                    navController.navigate(Screen.PagesProfileAndListOfDialogsScreen.route)
                },
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