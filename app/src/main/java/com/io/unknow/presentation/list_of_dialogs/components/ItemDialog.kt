package com.io.unknow.presentation.list_of_dialogs.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.io.unknow.R
import com.io.unknow.presentation.ui.theme.Shadow

@Composable
fun ItemDialog(
    nameUser: String,
    lastMessage: String,
    isMyMessage: Boolean,
    onClick:() -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.primary)
            .clickable { onClick() }
            .shadow(Shadow)
    ) {
        Image(
            painter = painterResource(id = R.mipmap.icon),
            contentDescription = stringResource(id = R.string.icon_user)
        )
        Column() {
            Text(text = nameUser)
            Text(
                text = buildAnnotatedString {
                    if (isMyMessage) {
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colors.onBackground,
                            )
                        ) {
                            append(stringResource(id = R.string.you))
                        }
                    }
                    append(lastMessage)
                }
            )
        }
    }

}