package com.drip.dripapplication.data.utils

import com.drip.dripapplication.domain.repository.PreferencesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response

class AddTokenHeaderInterceptor(private val repository: PreferencesRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token: String? = repository.getToken()
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