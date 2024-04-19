/*
 * This file is part of UNN-Android.
 *
 * UNN-Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UNN-Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with UNN-Android.  If not, see <https://www.gnu.org/licenses/>.
 */

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