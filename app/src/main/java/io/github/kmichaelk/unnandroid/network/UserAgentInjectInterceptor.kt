package io.github.kmichaelk.unnandroid.network

import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInjectInterceptor(
    private val userAgent: String
) : Interceptor {

    companion object {
        const val CHROME_ON_WINDOWS = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().newBuilder().header("User-Agent", userAgent).build())
    }
}