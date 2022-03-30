package com.io.unknow.presentation.chat.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.io.domain.model.MessageType
import com.io.unknow.presentation.ui.theme.UnknowAppTheme
import com.io.unknow.util.Constants.MY_TEST_PICTURE

@Composable
fun Message(
    isMyMessage:Boolean,
    message: MessageType
) {
    Row() {
        Box() {
            
        }
    }
}

@Preview
@Composable
fun PreviewMyTextMessage(){
    UnknowAppTheme {
        Message(
            isMyMessage = true,
            message = MessageType.TextType("Hello")
        )
    }
}

@Preview
@Composable
fun PreviewYouTextMessage(){
    UnknowAppTheme {
        Message(
            isMyMessage = false,
            message = MessageType.TextType("Hello")
        )
    }
}

@Preview
@Composable
fun PreviewMyPictureMessage(){
    UnknowAppTheme {
        Message(
            isMyMessage = true,
            message = MessageType.PictureType(MY_TEST_PICTURE)
        )
    }
}

@Preview
@Composable
fun PreviewYouPictureMessage(){
    UnknowAppTheme {
        Message(
            isMyMessage = false,
            message = MessageType.PictureType(MY_TEST_PICTURE)
        )
    }
}
        