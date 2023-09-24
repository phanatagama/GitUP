package com.github.gituser.data.common

import com.github.gituser.ui.common.SettingPreferences
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor constructor(private val pref: SettingPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
//        val token = pref.getToken()
        val newRequest = chain.request().newBuilder()
//            .addHeader("Authorization", token)
            .build()
        return chain.proceed(newRequest)
    }
}