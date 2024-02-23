package io.github.kmichaelk.unnandroid.exceptions

import java.io.IOException

class NetworkConnectivityException(
    message: String = "Connectivity error",
    cause: Exception? = null
) : IOException(message, cause)