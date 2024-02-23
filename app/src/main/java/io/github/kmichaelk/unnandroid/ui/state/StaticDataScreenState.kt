package io.github.kmichaelk.unnandroid.ui.state

data class StaticDataScreenState<T>(
    val data: T? = null,
    val error: UiError? = null,
)
