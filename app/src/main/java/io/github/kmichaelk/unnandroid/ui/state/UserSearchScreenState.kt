package io.github.kmichaelk.unnandroid.ui.state

data class UserSearchScreenState<T>(
    val query: String = "",
    val isSearching: Boolean = false,
    val error: UiError? = null,

    val perPage: Int = 20,
    val offset: Int = 0
)