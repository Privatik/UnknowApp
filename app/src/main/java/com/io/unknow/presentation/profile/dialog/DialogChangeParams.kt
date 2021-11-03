package com.io.unknow.presentation.profile.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.io.unknow.presentation.profile.model.ProfileParams
import com.io.unknow.presentation.ui.theme.DialogCorner
import com.io.unknow.presentation.ui.theme.SpaceSmall

@Composable
fun DialogChangeParams(
    listParams: List<ProfileParams>,
    onChangeParams: (List<ProfileParams>) -> Unit,
    onClose: () -> Unit
){
    Dialog(
        onDismissRequest = { onClose() },
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart = DialogCorner,
                topEnd = DialogCorner,
                bottomStart = DialogCorner,
                bottomEnd = 0.dp
            ),
            color = MaterialTheme.colors.background
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(SpaceSmall)
            ) {

            }
        }
    }
}