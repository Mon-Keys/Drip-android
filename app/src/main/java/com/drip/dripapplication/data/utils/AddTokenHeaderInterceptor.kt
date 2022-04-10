package com.drip.dripapplication.data.utils

import okhttp3.Interceptor
import okhttp3.Response

class AddTokenHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "52df637b000746e5aceeff75394cf68a"
        val originalRequest = chain.request()

        val request = if (token != null) {
            originalRequest.newBuilder()
                .header("Cookie", "sessionId=$token")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(request)
    }
}