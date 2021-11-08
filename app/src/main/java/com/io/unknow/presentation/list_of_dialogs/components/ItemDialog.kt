package com.io.unknow.presentation.list_of_dialogs.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.io.unknow.R
import com.io.unknow.presentation.ui.theme.*

@Composable
fun ItemDialog(
    nameUser: String,
    lastMessage: String,
    isMyMessage: Boolean,
    onClick:() -> Unit
){
    Card(
        shape = RectangleShape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(SizePictureMedium)
                .background(MaterialTheme.colors.primary)
                .clickable { onClick() }
        ) {
            Image(
                modifier = Modifier
                    .size(SizePictureMedium),
                contentScale = ContentScale.FillBounds,
                painter = painterResource(id = R.mipmap.icon),
                contentDescription = stringResource(id = R.string.icon_user)
            )
            Column(
                modifier = Modifier
                    .fillMaxHeight(0.5f)
                    .padding(top = PaddingSmall),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = nameUser
                )
                Text(
                    text = buildAnnotatedString {
                        if (isMyMessage) {
                            withStyle(
                                SpanStyle(
                                    color = MaterialTheme.colors.onSurface,
                                )
                            ) {
                                append("${stringResource(id = R.string.you)} ")
                            }
                        }
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colors.surface,
                            )
                        ) {
                            append(lastMessage)
                        }
                    }
                )
            }
        }
    }
}