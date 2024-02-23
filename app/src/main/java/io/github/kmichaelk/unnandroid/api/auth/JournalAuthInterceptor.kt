package io.github.kmichaelk.unnandroid.api.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import io.github.kmichaelk.unnandroid.api.service.JournalService
import io.github.kmichaelk.unnandroid.utils.extractCookieValue
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class JournalAuthInterceptor(
    private val service: JournalService,
    private val prefs: SharedPreferences,
    private val authDataHolder: AuthDataHolder,
) : Interceptor {

    companion object {
        private const val PREFS_PREFIX = "journal_"

        //
        private const val PREF_PHPSESSID = PREFS_PREFIX + "phpsessid"
        internal const val PREF_LOGIN_C = PREFS_PREFIX + "login"
        private const val PREF_HASH_C = PREFS_PREFIX + "hash"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(injectAuthData(chain.request()))

        if (response.code == 302) {
            response.close()
            val credentials = authDataHolder.getCredentials()
            runBlocking {
                val authResponse = service.login(
                    login = credentials.username,
                    password = credentials.password
                )
                val body = authResponse.body()
                if (body != null && body.contains("OK")) { // fuck...
                    val cookies = authResponse.headers().values("Set-Cookie")

                    prefs.edit {
                        putString(PREF_PHPSESSID, extractCookieValue(cookies, "PHPSESSID"))
                        putString(PREF_LOGIN_C, extractCookieValue(cookies, "login"))
                        putString(PREF_HASH_C, extractCookieValue(cookies, "hash"))
                    }
                } else {
                    // authDataHolder.logout()
                }
            }

            response = chain.proceed(injectAuthData(chain.request()))
        }

        return response
    }

    private fun injectAuthData(request: Request): Request =
        request.newBuilder()
            .header(
                "Cookie",
                "PHPSESSID=${prefs.getString(PREF_PHPSESSID, "")!!
                }; login=${
                    prefs.getString(
                        PREF_LOGIN_C,
                        ""
                    )!!
                }; hash=${
                    prefs.getString(
                        PREF_HASH_C, ""
                    )!!
                }"
            )
            .build()
}