package com.io.unknow.presentation.list_of_dialogs.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.io.unknow.R
import com.io.unknow.presentation.ui.theme.SpaceSmall
import timber.log.Timber
import kotlin.math.roundToInt

@Composable
fun CollapsingToolbarWithOutlinedTextFiled(
    searchText: String,
    onChangeSearchText: (String) -> Unit,
    toolbarHeight: Dp,
    content: @Composable BoxScope.() -> Unit
) {
    val titleHeight = toolbarHeight.times(0.35f)
    val textFieldHeight = toolbarHeight.div(0.65f)
    val toolbarHeightPx = with(LocalDensity.current){
        toolbarHeight.roundToPx().toFloat()
    }

    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }

    Box(
        modifier =  Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(toolbarHeight)
                .padding(start = 5.dp, top = 5.dp)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                modifier = Modifier
                    .height(titleHeight),
                text = stringResource(id = R.string.messages),
                color = MaterialTheme.colors.onPrimary
            )
            Spacer(modifier = Modifier.height(SpaceSmall))
            OutlinedTextField(
                modifier = Modifier.height(textFieldHeight),
                value = searchText,
                onValueChange = {
                    onChangeSearchText(it)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colors.surface,
                    focusedBorderColor = Color.White,
                    unfocusedBorderColor = MaterialTheme.colors.surface
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(id = R.string.search),
                        tint = MaterialTheme.colors.surface
                    )
                },
                placeholder = {
                    Text(
                        text = stringResource(id = R.string.search),
                        color = MaterialTheme.colors.surface
                    )
                }
            )
            Spacer(modifier = Modifier.height(SpaceSmall))
        }
        content()
    }
}