package io.github.kmichaelk.unnandroid.utils

import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
inline fun debounced(
    debounceTime: Long = 1000L,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    crossinline onClick: () -> Unit,
): () -> Unit {
    var lastTimeClicked by remember { mutableStateOf(0L) }
    var job by remember { mutableStateOf<Job?>(null) }

    return {
        job?.cancel()
        job = null

        val now = SystemClock.uptimeMillis()
        val elapsed = now - lastTimeClicked

        if (elapsed > debounceTime) {
            onClick()
        } else {
            job = coroutineScope.launch {
                delay(debounceTime - elapsed)
                onClick()
                job = null
            }
        }

        lastTimeClicked = now
    }
}