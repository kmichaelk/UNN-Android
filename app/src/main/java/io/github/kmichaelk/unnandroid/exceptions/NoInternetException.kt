package io.github.kmichaelk.unnandroid.exceptions

import java.io.IOException

class NoInternetException(
    message: String = "Internet connection is unavailable",
    cause: Exception? = null
) : IOException(message, cause)