package com.io.unknow.presentation.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.io.unknow.R
import com.io.unknow.domain.enums.Sex
import com.io.unknow.presentation.components.DatePickerDialog
import com.io.unknow.presentation.components.StandardTextField
import com.io.unknow.presentation.ui.theme.SpaceLarge
import com.io.unknow.presentation.ui.theme.SpaceMedium
import com.io.unknow.presentation.ui.theme.SpaceSmall
import com.io.unknow.util.Constants.EMPTY_BIRT_DAY

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
){
    RegisterScreenPortrait(
        navController = navController,
        viewModel = viewModel
    )
}

@Composable
fun RegisterScreenPortrait(
    navController: NavController,
    viewModel: RegisterViewModel
){
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SpaceMedium),
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
        Spacer(modifier = Modifier.height(SpaceMedium))
        Text(
            text = stringResource(id = R.string.sex),
            color = MaterialTheme.colors.onPrimary
        )
        Spacer(modifier = Modifier.height(SpaceSmall))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.sex_men),
                color = Color.White
            )
            Spacer(modifier = Modifier.width(SpaceSmall))
            RadioButton(
                selected = viewModel.sexRation.value == Sex.MEN,
                onClick = {
                    if (viewModel.sexRation.value != Sex.MEN) {
                        viewModel.setSexRation(Sex.MEN)
                    }
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.White,
                    unselectedColor = MaterialTheme.colors.onPrimary
                )
            )
            Spacer(modifier = Modifier.width(SpaceMedium))
            Text(
                text = stringResource(id = R.string.sex_women),
                color = Color.White
            )
            Spacer(modifier = Modifier.width(SpaceSmall))
            RadioButton(
                selected = viewModel.sexRation.value == Sex.WOMEN,
                onClick = {
                    if (viewModel.sexRation.value != Sex.WOMEN) {
                        viewModel.setSexRation(Sex.WOMEN)
                    }
                },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color.White,
                    unselectedColor = MaterialTheme.colors.onPrimary
                )
            )
        }
        Spacer(modifier = Modifier.height(SpaceMedium))
        Text(
            text = stringResource(id = R.string.birth_day),
            color = MaterialTheme.colors.onPrimary
        )
        Spacer(modifier = Modifier.height(SpaceSmall))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                DatePickerDialog(context = context){
                    viewModel.setDatBirthDay(it)
                }
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = viewModel.birthDayText.value.let {
                    when (it) {
                        "" -> MaterialTheme.colors.surface
                        EMPTY_BIRT_DAY -> Color.Red
                        else -> MaterialTheme.colors.onSurface
                    }
                }
            )
        ) {
            Text(
                text = viewModel.birthDayText.value.let {
                    if (it == "")
                        stringResource(id = R.string.no_date_selected)
                    else
                        it
                },
                textAlign = TextAlign.Center,
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }
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
                    navController.popBackStack()
                },
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
                onClick = {
                   // viewModel.setDatBirthDay(EMPTY_BIRT_DAY)
                },
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