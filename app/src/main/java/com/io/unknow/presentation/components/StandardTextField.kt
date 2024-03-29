package com.io.unknow.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.io.unknow.R

@Composable
fun StandardTextField(
    text: String,
    hint: String,
    error: String = "",
    maxLength: Int = 40,
    modifier: Modifier = Modifier.fillMaxWidth(),
    backgroundColor: Color = Color.Transparent,
    focusedIndicatorColor: Color = Color.Transparent,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit = {}
){
    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    val isPasswordToggleDisplayed by remember {
        mutableStateOf(keyboardType == KeyboardType.Password)
    }

    Column(modifier) {
        TextField(
            value = text,
            textStyle = MaterialTheme.typography.h3,
            onValueChange = {
                if (it.length <= maxLength) {
                    onValueChange(it)
                }
            },
            placeholder = {
                Text(
                    text = hint,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.body1
                )
            },
            visualTransformation = if (!isPasswordVisible && isPasswordToggleDisplayed) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            singleLine = true,
            isError = error != "",
            trailingIcon = {
                if (isPasswordToggleDisplayed) {
                    IconButton(onClick = {
                        isPasswordVisible = !isPasswordVisible
                    }) {
                        Icon(
                            imageVector =
                            if (isPasswordVisible) {
                                Icons.Filled.VisibilityOff
                            } else {
                                Icons.Filled.Visibility
                            },
                            contentDescription =
                            if (isPasswordVisible) {
                                stringResource(id = R.string.visible_password)
                            } else {
                                stringResource(id = R.string.gone_password)
                            }
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = backgroundColor,
                focusedIndicatorColor = focusedIndicatorColor,
                cursorColor = MaterialTheme.colors.onSurface,
                errorIndicatorColor = Color.Red
            )
        )
        if (error != "") {
            Text(
                text = error,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Red,
                textAlign = TextAlign.End
            )
        }
    }
}