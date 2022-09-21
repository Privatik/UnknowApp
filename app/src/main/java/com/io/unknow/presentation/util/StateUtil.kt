package com.io.unknow.presentation.util

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@SuppressLint("FlowOperatorInvokedInComposition", "StateFlowValueCalledInComposition")
@Composable
fun <T> StateFlow<T>.collectAsStateInLifeCycle(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext
): State<T> {
    val lifecycleOwner = LocalLifecycleOwner.current
    return flowWithLifecycle(lifecycleOwner.lifecycle, minActiveState).collectAsState(value, context)
}
