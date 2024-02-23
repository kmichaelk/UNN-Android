package io.github.kmichaelk.unnandroid.ui.composables

import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalViewConfiguration
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LongPressHandler(
    interactionSource: InteractionSource,
    onClick: () -> Unit,
    onLongPress: () -> Unit
) {
    val viewConfiguration = LocalViewConfiguration.current
    LaunchedEffect(interactionSource) {
        var isLongClick = false

        interactionSource.interactions.collectLatest { interaction ->
            when (interaction) {
                is PressInteraction.Press -> {
                    isLongClick = false
                    delay(viewConfiguration.longPressTimeoutMillis)
                    isLongClick = true
                    onClick()
                }
                is PressInteraction.Release -> {
                    if (isLongClick.not()) {
                        onLongPress()
                    }
                }
            }
        }
    }
}