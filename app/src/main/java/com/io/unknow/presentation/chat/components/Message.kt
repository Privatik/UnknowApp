package com.io.unknow.presentation.chat.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.io.unknow.presentation.chat.MessageUI
import com.io.unknow.presentation.ui.theme.UnknowAppTheme

@Composable
fun Message(
    isMyMessage:Boolean,
    message: MessageUI
) {
    if (isMyMessage){

    } else {

    }
}

@Composable
private fun MyMessage(message: MessageUI){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = message.message)
        Text(
            text = message.time.toString(),
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun OtherMessage(message: MessageUI){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = message.message)
        Text(
            text = message.time.toString(),
            modifier = Modifier.align(Alignment.Start)
        )
    }
}

@Preview
@Composable
fun PreviewMyTextMessage(){
    UnknowAppTheme {
        Message(
            isMyMessage = true,
            message = MessageUI("", "Hello", 1111111)
        )
    }
}

@Preview
@Composable
fun PreviewYouTextMessage(){
    UnknowAppTheme {
        Message(
            isMyMessage = false,
            message = MessageUI("", "Hello", 1111111)
        )
    }
}

@Preview
@Composable
fun PreviewMyPictureMessage(){
    UnknowAppTheme {
        Message(
            isMyMessage = true,
            message = MessageUI("", "Hello", 1111111)
        )
    }
}

@Preview
@Composable
fun PreviewYouPictureMessage(){
    UnknowAppTheme {
        Message(
            isMyMessage = false,
            message = MessageUI("", "Hello", 1111111)
        )
    }
}
        