package io.github.kmichaelk.unnandroid.utils

fun extractCookieValue(response: retrofit2.Response<*>, cookieName: String) : String? =
    extractCookieValue(response.headers().values("Set-Cookie"), cookieName)

fun extractCookieValue(cookies: List<String>, cookieName: String) : String? {
    val hdrVal = "${cookieName}="
    for (cookie in cookies) {
        if (cookie.startsWith(hdrVal)) {
            return extractCookieValue(cookie)
        }
    }
    return null
}

fun extractCookieValue(setCookie: String): String =
    setCookie.substring(setCookie.indexOf("=") + 1, setCookie.indexOf(";"))