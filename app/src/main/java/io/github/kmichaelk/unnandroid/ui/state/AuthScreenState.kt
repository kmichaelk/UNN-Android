package io.github.kmichaelk.unnandroid.ui.state

data class AuthScreenState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val finished: Boolean = false,
)
