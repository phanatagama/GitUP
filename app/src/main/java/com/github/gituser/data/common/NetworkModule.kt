package com.github.gituser.data.common

import com.github.gituser.BuildConfig
import com.github.gituser.ui.common.SettingPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttp: OkHttpClient) : Retrofit {
        return Retrofit.Builder().apply {
            addConverterFactory(GsonConverterFactory.create())
            client(okHttp)
            baseUrl(BuildConfig.BASE_URL)
        }.build()
    }

    @Singleton
    @Provides
    fun provideOkHttp(requestInterceptor: RequestInterceptor) : OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
            addInterceptor(requestInterceptor)
        }.build()
    }

    @Provides
    fun provideRequestInterceptor(prefs: SettingPreferences) : RequestInterceptor {
        return RequestInterceptor(prefs)
    }

}