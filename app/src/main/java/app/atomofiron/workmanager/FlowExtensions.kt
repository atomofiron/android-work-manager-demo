package app.atomofiron.workmanager

import androidx.fragment.app.Fragment
import androidx.lifecycle.coroutineScope
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect


fun <T> dataFlow() = MutableSharedFlow<T>(
    replay = 1,
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST,
)

fun <T> dataFlow(value: T) = MutableSharedFlow<T>(
    replay = 1,
    extraBufferCapacity = 1,
    onBufferOverflow = BufferOverflow.DROP_OLDEST,
).apply {
    this.value = value
}

fun <T> eventFlow(lossless: Boolean) = MutableSharedFlow<T>(
    replay = 0,
    extraBufferCapacity = if (lossless) Int.MAX_VALUE else 1,
    onBufferOverflow = if (lossless) BufferOverflow.SUSPEND else BufferOverflow.DROP_OLDEST,
)

var <T> MutableSharedFlow<T>.value: T
    get() = replayCache.last()
    set(value) {
        GlobalScope.launch(Dispatchers.Unconfined, CoroutineStart.UNDISPATCHED) {
            emit(value)
        }
    }

val <T> SharedFlow<T>.value: T
    get() = replayCache.last()

val <T> SharedFlow<T>.valueOrNull: T
    get() {
        say("valueOrNull 0")
        val cache = replayCache
        say("valueOrNull 1")
        val lastOrNull = cache.lastOrNull()
        say("valueOrNull 2")
        return lastOrNull!!
    }

inline fun <T> Flow<T>.collectOn(coroutineScope: CoroutineScope, crossinline collector: suspend (T) -> Unit) {
    coroutineScope.launch {
        // Flow.collect() блочит корутину, поэтому каждый раз нужно запускать новую
        collect(collector)
    }
}

inline fun <T> Fragment.collectOnView(flow: Flow<T>, crossinline collector: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycle.coroutineScope.launch {
        flow.collect(collector)
    }
}
