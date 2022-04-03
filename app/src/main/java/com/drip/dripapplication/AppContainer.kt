package com.drip.dripapplication

import com.drip.dripapplication.data.remote.DripApi
import com.drip.dripapplication.data.repository.UserRepositoryImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import timber.log.Timber

// Container of objects shared across the whole app
class AppContainer {
    // Since you want to expose userRepository out of the container, you need to satisfy
    // its dependencies as you did before

    private val okhttpClient = OkHttpClient.Builder()
        .addInterceptor (HttpLoggingInterceptor{
            Timber.tag("Network").d(it)
        }
            .setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://drip.monkeys.team/")
        .client(okhttpClient)
        .build()
        .create(DripApi::class.java)


    // userRepository is not private; it'll be exposed
    val userRepository = UserRepositoryImpl(retrofit)
}