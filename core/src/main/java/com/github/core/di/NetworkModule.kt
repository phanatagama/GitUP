package com.github.core.di

import android.content.Context
import com.github.core.BuildConfig
import com.github.core.data.common.RequestInterceptor
import com.github.core.data.common.SettingPreferences
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
@Suppress("SpellCheckingInspection")
fun provideOkHttp(requestInterceptor: RequestInterceptor): OkHttpClient {

    val hostname = "api.github.com"
    val certificate = CertificatePinner.Builder()
        .add(hostname, "sha256/jFaeVpA8UQuidlJkkpIdq3MPwD0m8XbuCRbJlezysBE=")
        .add(hostname, "sha256/Wec45nQiFwKvHtuHxSAMGkt19k+uPSw9JlEkxhvYPHk=")
        .add(hostname, "sha256/Jg78dOE+fydIGk19swWwiypUSR6HWZybfnJG/8G7pyM=")
        .add(hostname, "sha256/e0IRz5Tio3GA1Xs4fUVWmH1xHDiH2dMbVtCBSkOIdqM=")
        .build()
    return OkHttpClient.Builder().apply {
        connectTimeout(60, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
        writeTimeout(60, TimeUnit.SECONDS)
        addInterceptor(requestInterceptor)
        certificatePinner(certificate)
    }.build()
}

fun provideRetrofit(okHttp: OkHttpClient): Retrofit = Retrofit.Builder().apply {
    addConverterFactory(GsonConverterFactory.create())
    client(okHttp)
    baseUrl(BuildConfig.BASE_URL)
}.build()


fun provideRequestInterceptor(): RequestInterceptor =
    RequestInterceptor()

fun provideSettingPreferences(context: Context): SettingPreferences = SettingPreferences(context)

val networkModule = module {
    single { provideRequestInterceptor() }
    single { provideOkHttp(get()) }
    single { provideRetrofit(get()) }
    single { provideSettingPreferences(androidContext()) }
}