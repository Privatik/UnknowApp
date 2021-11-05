package com.io.unknow.presentation.list_of_dialogs.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import com.io.unknow.R
import com.io.unknow.presentation.ui.theme.PaddingPostSmall
import com.io.unknow.presentation.ui.theme.PaddingSmall
import com.io.unknow.presentation.ui.theme.Shadow
import com.io.unknow.presentation.ui.theme.TextMedium
import kotlin.math.roundToInt

@Composable
fun CollapsingToolbarWithOutlinedTextFiled(
    searchText: String,
    onChangeSearchText: (String) -> Unit,
    toolbarOffsetHeightPx: Float,
    toolbarHeightExpandedPx: Float,
    toolbarHeightCollapsedPx: Float,
    nestedScrollConnection: NestedScrollConnection,
    content: @Composable BoxScope.() -> Unit
) {
    val toolbarHeightExpanded = toolbarHeightExpandedPx.roundToInt()

    val color = MaterialTheme.colors.primary
    Column {
        val offset = ((toolbarOffsetHeightPx - toolbarHeightCollapsedPx) / (toolbarHeightExpandedPx - toolbarHeightCollapsedPx))
        val paddingOffset = with(LocalDensity.current){offset.toDp().times(10)}
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { toolbarHeightCollapsedPx.toDp() })
                .drawBehind {
                    drawRect(
                        color = color,
                        alpha = 1f - offset
                    )
                }
                .padding(start = PaddingSmall + paddingOffset, top = PaddingSmall + paddingOffset),
            text = stringResource(id = R.string.messages),
            fontSize = TextMedium,
            color = MaterialTheme.colors.onPrimary
        )
        Spacer(modifier = Modifier
            .height(Shadow)
            .background(Color.Black)
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(nestedScrollConnection)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = PaddingPostSmall,
                        end = PaddingPostSmall
                    )
                    .offset
                    {
                        IntOffset(
                            x = 0,
                            y = (toolbarOffsetHeightPx.roundToInt()) - toolbarHeightExpanded
                        )
                    }
                    .graphicsLayer {
                        alpha = offset
                    },
                value = searchText,
                onValueChange = {
                    onChangeSearchText(it)
                },
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
                },
                enabled = toolbarOffsetHeightPx == toolbarHeightExpandedPx,
                readOnly = toolbarOffsetHeightPx != toolbarHeightExpandedPx,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colors.onSecondary,
                    cursorColor =  MaterialTheme.colors.onSecondary,
                    focusedBorderColor = MaterialTheme.colors.surface,
                    unfocusedBorderColor = MaterialTheme.colors.surface,
                )
            )
            content()
        }
    }
}