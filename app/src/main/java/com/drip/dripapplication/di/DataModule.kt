package com.drip.dripapplication.di

import android.content.Context
import com.drip.dripapplication.data.remote.DripApi
import com.drip.dripapplication.data.repository.*
import com.drip.dripapplication.data.utils.AddTokenHeaderInterceptor
import com.drip.dripapplication.domain.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideCookieJar(repository: PreferencesRepository): CookieJar {
        return object : CookieJar {
            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                return emptyList()
            }

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookies.forEach {
                    if (it.name == "sessionId"){
                        repository.saveToken(it.value)
                    }
                }
            }

        }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply { setLevel(HttpLoggingInterceptor.Level.BODY) }

    @Provides
    @Singleton
    fun provideTokenInterceptor(repository: PreferencesRepository): AddTokenHeaderInterceptor = AddTokenHeaderInterceptor(repository)

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cookieJar: CookieJar,
        tokenInterceptor: AddTokenHeaderInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient{
        return OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(loggingInterceptor)
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

    @Provides
    @Singleton
    fun providePreferencesRepository(@ApplicationContext context: Context): PreferencesRepository = PreferencesRepositoryImpl(context.getSharedPreferences(PreferencesRepositoryImpl.DATASTORE_NAME, Context.MODE_APPEND))
}
