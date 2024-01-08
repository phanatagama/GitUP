package com.github.core.data.common

import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
//        val token = pref.getToken()
        val newRequest = chain.request().newBuilder()
//            .addHeader("Authorization", token)
            .build()
        return chain.proceed(newRequest)
    }
}