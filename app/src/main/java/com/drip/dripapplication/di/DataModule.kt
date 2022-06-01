package com.drip.dripapplication.di

import com.drip.dripapplication.data.remote.DripApi
import com.drip.dripapplication.data.repository.*
import com.drip.dripapplication.data.utils.AddTokenHeaderInterceptor
import com.drip.dripapplication.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(AddTokenHeaderInterceptor())
            .addInterceptor(
                HttpLoggingInterceptor {
                    Timber.tag("Network").d(it)
                }
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://drip.monkeys.team/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDripApi(retrofit: Retrofit): DripApi = retrofit.create(DripApi::class.java)

    @Provides
    @Singleton
    fun provideUserRepository(api: DripApi): UserRepository = UserRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideLikeRepository(api: DripApi): LikeRepository = LikeRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideFeedRepository(api: DripApi): FeedRepository = FeedRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideAuthRepository(api: DripApi): AuthRepository = AuthRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideProfileRepository(api: DripApi): ProfileRepository = ProfileRepositoryImpl(api)
}