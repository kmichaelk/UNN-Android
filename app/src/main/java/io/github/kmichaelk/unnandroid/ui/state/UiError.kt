package io.github.kmichaelk.unnandroid.ui.state

import io.github.kmichaelk.unnandroid.exceptions.NoInternetException
import java.io.IOException

data class UiError(
    val title: String,
    val message: String,
) {
    companion object {
        fun from(exception: Exception) = when (exception) {
            is NoInternetException -> UiError("Сеть недоступна", "Попробуйте еще раз")
            is IOException -> UiError("Ошибка сети", "Попробуйте еще раз")
            else -> UiError("Неизвестная ошибка", "Попробуйте еще раз")
        }
    }
}
