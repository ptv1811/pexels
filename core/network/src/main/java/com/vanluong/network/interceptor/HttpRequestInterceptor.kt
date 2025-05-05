package com.vanluong.network.interceptor

import com.vanluong.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 *  This class is responsible for intercepting HTTP requests and adding headers.
 */
class HttpRequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val request = originalRequest.newBuilder()
            .addHeader("Authorization", BuildConfig.PEXELS_API_KEY)
            .url(originalRequest.url)
            .build()
        return chain.proceed(request)
    }
}