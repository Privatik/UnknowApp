package com.io.unknow.presentation.list_of_dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.io.unknow.R
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.io.unknow.presentation.ui.theme.SpaceSmall

@Composable
fun ListOfDialogsScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            modifier = Modifier
                .fillMaxWidth(),
            backgroundColor = MaterialTheme.colors.primary
        ) {
            Text(
                text = stringResource(id = R.string.messages),
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(SpaceSmall)
            )
        }
    }
}