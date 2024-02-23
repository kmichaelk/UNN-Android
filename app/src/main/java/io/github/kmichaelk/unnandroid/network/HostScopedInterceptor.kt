package io.github.kmichaelk.unnandroid.network

import okhttp3.Interceptor
import okhttp3.Response

class HostScopedInterceptor(
    private val interceptor: Interceptor,
    private val host: String,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (request.url.host == host) {
            return interceptor.intercept(chain)
        }
        return chain.proceed(request)
    }
}