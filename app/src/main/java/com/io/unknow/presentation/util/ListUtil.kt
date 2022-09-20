package com.io.unknow.presentation.util

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import io.pagination.common.rememberPagingLazyListState

@Composable
public fun rememberPagingAndAutoScrollingLazyListState(
    initialFirstVisibleItemIndex: Int = 0,
    initialFirstVisibleItemScrollOffset: Int = 0,
    nextPageLoading: () -> Unit,
    previousPageLoading: () -> Unit
) = rememberPagingLazyListState(
    initialFirstVisibleItemIndex,
    initialFirstVisibleItemScrollOffset,
    nextPageLoading,
    previousPageLoading
).apply{
    var total by remember { mutableStateOf(0) }

    LaunchedEffect(layoutInfo.totalItemsCount){
        val timeTotal = layoutInfo.totalItemsCount
        if ((layoutInfo.visibleItemsInfo.firstOrNull()?.index ?: 0) + 1 >= timeTotal - total) {
            scrollToItem(0)
        }
        total = layoutInfo.totalItemsCount
    }
}