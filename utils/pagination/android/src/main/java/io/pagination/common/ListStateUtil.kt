package io.pagination.common

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue

@Composable
public fun rememberPagingLazyListState(
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0,
    nextPageLoading: () -> Unit,
    previousPageLoading: () -> Unit
) = rememberLazyListState(
    initialFirstVisibleItemIndex,
    initialFirstVisibleItemScrollOffset
).apply{
    val lastVisibleIndex by derivedStateOf { isVisibleLastIndex() }
    val firstVisibleIndex by derivedStateOf { isVisibleFirstIndex() }

    LaunchedEffect(firstVisibleIndex, lastVisibleIndex){
        if (layoutInfo.visibleItemsInfo.firstOrNull()?.index == layoutInfo.visibleItemsInfo.lastOrNull()?.index){
            return@LaunchedEffect
        }
        if (firstVisibleIndex){
            previousPageLoading()
        }
        if (lastVisibleIndex){
            nextPageLoading()
        }
    }
}

private fun LazyListState.isVisibleLastIndex() = layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1
private fun LazyListState.isVisibleFirstIndex() = layoutInfo.visibleItemsInfo.firstOrNull()?.index == 0