package io.github.kmichaelk.unnandroid.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class SourceJsonNormalizer : Interceptor {

    // source.unn.ru responds with ")]}',\n" anti-XSSI JSON prefix

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val body = response.body
        if (body != null && "application/json; charset=UTF-8" == response.header("Content-Type", null)) {
            return response.newBuilder()
                .body(
                    body.string()
                        .substring(6) // ")]}',\n".length() == 6
                        .toResponseBody(body.contentType())
                )
                .build()
        }
        return response
    }
}