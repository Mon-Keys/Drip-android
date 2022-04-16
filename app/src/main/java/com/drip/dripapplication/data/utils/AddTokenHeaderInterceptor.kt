package com.drip.dripapplication.data.utils

import okhttp3.Interceptor
import okhttp3.Response

class AddTokenHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "f21dde493d4c76c3e8116fd2172b9ec7"
        val csfr = "98d3f70e-0bd0-4054-4391-a2914c8414f2"
        val originalRequest = chain.request()

        val request = if (token != null) {
            originalRequest.newBuilder()
                .header("Cookie", "sessionId=$token")
                //.addHeader("Cookie", "csrf=$csfr")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(request)
    }
}