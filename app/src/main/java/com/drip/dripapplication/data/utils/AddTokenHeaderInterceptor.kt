package com.drip.dripapplication.data.utils

import okhttp3.Interceptor
import okhttp3.Response

class AddTokenHeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = "92df2a35f1c60eb490d856a5194c87d5"
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