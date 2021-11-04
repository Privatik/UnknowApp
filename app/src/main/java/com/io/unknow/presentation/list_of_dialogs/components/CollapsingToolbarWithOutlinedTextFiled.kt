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
import com.io.unknow.presentation.ui.theme.*
import com.io.unknow.presentation.ui.theme.PaddingSmall
import timber.log.Timber
import kotlin.math.roundToInt

@Composable
fun CollapsingToolbarWithOutlinedTextFiled(
    searchText: String,
    onChangeSearchText: (String) -> Unit,
    toolbarOffsetHeightPx: Float,
    toolbarHeightExpandedPx: Float,
    nestedScrollConnection: NestedScrollConnection,
    content: @Composable BoxScope.() -> Unit
) {
    val toolbarHeightExpanded = toolbarHeightExpandedPx.roundToInt()

    Column(
        modifier  = Modifier.padding(
            start = PaddingPostSmall,
            top = PaddingPostSmall,
            end = PaddingPostSmall
        ),
    ) {
        Text(
            text = stringResource(id = R.string.messages),
            fontSize = TextMedium,
            color = MaterialTheme.colors.onPrimary
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
                        top = PaddingPostSmall,
                        end = PaddingPostSmall
                    )
                    .offset
                    {
                        IntOffset(x = 0, y = (toolbarOffsetHeightPx.roundToInt()) - toolbarHeightExpanded)
                    },
                value = searchText,
                onValueChange = {
                    onChangeSearchText(it)
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colors.surface,
                    focusedBorderColor = MaterialTheme.colors.surface,
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
            content()
        }
    }
}