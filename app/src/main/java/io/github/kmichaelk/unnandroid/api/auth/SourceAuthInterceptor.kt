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

package io.github.kmichaelk.unnandroid.api.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import io.github.kmichaelk.unnandroid.api.service.SourceService
import io.github.kmichaelk.unnandroid.utils.extractCookieValue
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class SourceAuthInterceptor(
    private val service: SourceService,
    private val prefs: SharedPreferences,
    private val authDataHolder: AuthDataHolder,
) : Interceptor {

    companion object {
        private const val PREFS_PREFIX = "source_"

        //
        private const val PREF_PHPSESSID  = PREFS_PREFIX + "phpsessid"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        runBlocking {
            // why just not return 401...
            val isSessionActive = service.checkSession("PHPSESSID=" + getSessionId())

            if (!isSessionActive) {
                val credentials = authDataHolder.getCredentials()
                val authResponse = service.login(
                    credentials.username,
                    credentials.password,
                )
                if (authResponse.code() == 302) {
                    val phpSessionId = extractCookieValue(authResponse.headers().values("Set-Cookie"), "PHPSESSID")
                    prefs.edit {
                        putString(PREF_PHPSESSID, phpSessionId)
                    }
                } else {
                    // authDataHolder.logout()
                }
            }
        }

        return chain.proceed(injectAuthData(chain.request()))
    }

    private fun injectAuthData(request: Request): Request =
        request.newBuilder()
            .header("Cookie", "PHPSESSID=" + getSessionId())
            .build()

    private fun getSessionId() = prefs.getString(PREF_PHPSESSID, "")!!
}