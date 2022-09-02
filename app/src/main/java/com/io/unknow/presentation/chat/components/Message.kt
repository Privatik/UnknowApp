package com.io.unknow.presentation.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import com.io.unknow.presentation.chat.model.MessageUI
import com.io.unknow.presentation.ui.theme.*

@Composable
fun Message(
    isMyMessage:Boolean,
    message: MessageUI
) {
    if (isMyMessage){
        SettingMessage(
            messageUI = message,
            arrangement = Arrangement.End,
            contentColor = Color.DarkGray
        ){
            Path().apply {
                moveTo(size.width - 20, size.height / 2)
                lineTo(size.width, size.height / 2)
                lineTo(size.width, size.height)
                close()
                drawPath(path = this, color = Color.DarkGray)
            }
        }
    } else {
        SettingMessage(
            messageUI = message,
            arrangement = Arrangement.Start,
            contentColor = Color.Black
        ){
            Path().apply {
                moveTo(20f, size.height / 2)
                lineTo(0f, size.height / 2)
                lineTo(0f, size.height)
                close()
                drawPath(path = this, color = Color.Black)
            }
        }
    }
}

@Composable
private fun SettingMessage(
    messageUI: MessageUI,
    arrangement: Arrangement.Horizontal,
    contentColor: Color,
    modifier: Modifier = Modifier,
    drawEndForMessage: DrawScope.() -> Unit,
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = arrangement
    ) {
        Column(
            modifier = modifier
                .widthIn(MinWidthMessage, MaxWidthMessage)
                .padding(bottom = PaddingSmall)
                .padding(horizontal = PaddingPostSmall)
                .drawBehind(drawEndForMessage)
                .padding(vertical = PaddingPostSmall)
                .clip(Shapes.small)
                .background(contentColor)
                .padding(PaddingSmall)
        ) {
            Text(
                text = messageUI.message,
                color = Color.Gray,
                fontSize = TextMedium,
                style = Typography.body1
            )
            Spacer(modifier = Modifier.height(SpaceSmall))
            Text(
                text = messageUI.time.toString(),
                color = Color.LightGray,
                fontSize = TextPreSmall,
                modifier = Modifier.align(Alignment.End),
                style = Typography.body2
            )
        }
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
            message = MessageUI("", "Hello World World World", 1111111)
        )
    }
}

@Preview
@Composable
fun PreviewMyPictureMessage(){
    UnknowAppTheme {
        Message(
            isMyMessage = true,
            message = MessageUI("", "Hello World World", 1111111)
        )
    }
}

@Preview
@Composable
fun PreviewYouPictureMessage(){
    UnknowAppTheme {
        Message(
            isMyMessage = false,
            message = MessageUI("", "Hello World", 1111111)
        )
    }
}
        