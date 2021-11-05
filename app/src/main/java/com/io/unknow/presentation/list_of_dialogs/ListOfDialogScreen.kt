package com.io.unknow.presentation.list_of_dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.io.unknow.presentation.list_of_dialogs.components.CollapsingToolbarWithOutlinedTextFiled
import com.io.unknow.presentation.list_of_dialogs.components.ItemDialog
import com.io.unknow.presentation.ui.theme.SpaceSmall

@Composable
fun ListOfDialogsScreen(
    navController: NavController,
    viewModel: ListOfDialogViewModel = hiltViewModel(),
    heightToolbarCollapsed: Dp = 50.dp
){

    val heightToolbarExpanded: Dp = heightToolbarCollapsed.times(2.5f)

    val toolbarHeightCollapsedPx = with(LocalDensity.current){
        heightToolbarCollapsed.roundToPx().toFloat()
    }

    val toolbarHeightExpandedPx = with(LocalDensity.current){
        heightToolbarExpanded.roundToPx().toFloat()
    }

    val toolbarOffsetHeightPx = remember { mutableStateOf(toolbarHeightExpandedPx) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(toolbarHeightCollapsedPx, toolbarHeightExpandedPx)
                return Offset.Zero
            }
        }
    }

    CollapsingToolbarWithOutlinedTextFiled(
        searchText = viewModel.searchText.value,
        onChangeSearchText = {
            viewModel.setSearchText(search = it)
        },
        toolbarOffsetHeightPx = toolbarOffsetHeightPx.value,
        toolbarHeightExpandedPx = toolbarHeightExpandedPx,
        toolbarHeightCollapsedPx = toolbarHeightCollapsedPx,
        nestedScrollConnection = nestedScrollConnection
    ){
        LazyColumn(
            modifier = Modifier
                .padding(
                    PaddingValues(
                        top = with(LocalDensity.current) {(toolbarOffsetHeightPx.value - toolbarHeightCollapsedPx).toDp()},
                        start = SpaceSmall,
                        end = SpaceSmall
                    )))
        {
            repeat(30){ index ->
                item {
                    ItemDialog(
                        nameUser = "Alexander",
                        lastMessage = "Hello",
                        isMyMessage = index % 2 == 0,
                        onClick = {}
                    )
                    Spacer(modifier = Modifier.height(SpaceSmall))
                }
            }
        }
    }
}