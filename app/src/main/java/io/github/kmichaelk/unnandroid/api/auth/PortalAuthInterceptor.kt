package io.github.kmichaelk.unnandroid.api.auth

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import io.github.kmichaelk.unnandroid.api.service.PortalService
import io.github.kmichaelk.unnandroid.models.portal.PortalResponseWrapper
import io.github.kmichaelk.unnandroid.utils.extractCookieValue
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Invocation

class PortalAuthInterceptor(
    private val service: PortalService,
    private val prefs: SharedPreferences,
    private val authDataHolder: AuthDataHolder,
) : Interceptor {

    companion object {
        private const val PREFS_PREFIX = "portal_"

        //
        private const val PREF_PHPSESSID  = PREFS_PREFIX + "phpsessid"
        private const val PREF_RESTSESSID = PREFS_PREFIX + "restsessid"

        //
        private fun checkAuthResponse(setCookieHeaders: List<String>, prefs: SharedPreferences): Boolean {
            if (!setCookieHeaders.any { it.startsWith("BX_PORTAL_UNN_LOGIN=") }) {
                return false
            }

            extractCookieValue(setCookieHeaders, "PHPSESSID")?.let {
                prefs.edit {
                    putString(PREF_PHPSESSID, it)
                }
            }

            return true
        }
        fun checkAuthResponse(response: retrofit2.Response<*>, prefs: SharedPreferences): Boolean =
            checkAuthResponse(response.headers().values("Set-Cookie"), prefs)
    }

    init {
        prefs.edit {
            putString(PREF_PHPSESSID, "nolongervalid")
            putString(PREF_RESTSESSID, "nolongervalid")
        }
    }

    private val gson = Gson()

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(injectAuthData(chain.request()))

        val bxNewCsrf = response.header("X-Bitrix-New-Csrf") != null
        if (response.code == 401 || bxNewCsrf || "Authorize" == response.header("X-Bitrix-Ajax-Status", null)) {
            if (!bxNewCsrf && "application/json; charset=utf-8" == response.header("Content-Type", null)) {
                response.body?.let { saveRestSessionId(it) }
                response.close()
            } else {
                response.close() // ASAP
                //
                val credentials = authDataHolder.getCredentials()
                runBlocking {
                    val authResponse = service.login(
                        login = credentials.username,
                        password = credentials.password,
                        remember = PortalService.BitrixBool.Y
                    )
                    if (!checkAuthResponse(authResponse, prefs)) {
                        authDataHolder.logout()
                    } else if (bxNewCsrf) {
                        try {
                            service.verifySessionId(
                                cookie = "PHPSESSID=${prefs.getString(PREF_PHPSESSID, "")!!}",
                                sessionId = prefs.getString(PREF_RESTSESSID, "")!!
                            )
                        } catch (ex: HttpException) {
                            if (ex.code() == 401) {
                                ex.response()?.errorBody()?.let { saveRestSessionId(it) }
                            }
                        }
                    }
                }
            }

            response = chain.proceed(injectAuthData(chain.request()))
        }

        return response
    }

    private fun injectAuthData(request: Request): Request {
        val builder = request.newBuilder()

        request.tag(Invocation::class.java)?.method()?.let {
            if (it.getAnnotation(PortalService.InjectSessionIdQueryParam::class.java) != null) {
                builder.url(
                    request.url.newBuilder().setQueryParameter(
                        "sessid", prefs.getString(
                            PREF_RESTSESSID, ""
                        )
                    ).build()
                )
            }
            if (it.getAnnotation(PortalService.InjectCsrfTokenHeader::class.java) != null) {
                builder.header("X-Bitrix-Csrf-Token", prefs.getString(
                    PREF_RESTSESSID, ""
                )!!)
            }
        }

        builder.header("Cookie", "PHPSESSID=" + prefs.getString(PREF_PHPSESSID, "")!!)

        return builder.build()
    }

    private fun saveRestSessionId(body: ResponseBody) {
        gson.fromJson(body.string(), PortalResponseWrapper::class.java).sessionId?.let {
            prefs.edit {
                putString(PREF_RESTSESSID, it)
            }
        }
    }
}