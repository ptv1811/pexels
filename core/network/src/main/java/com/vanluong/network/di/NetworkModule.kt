package com.vanluong.network.di

import com.vanluong.network.BuildConfig
import com.vanluong.network.interceptor.HttpRequestInterceptor
import com.vanluong.network.service.PexelsClient
import com.vanluong.network.service.PexelsService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Call
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

/**
 * This Hilt module provides the necessary dependencies for network operations.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    /**
     * Provides an instance of OkHttpClient as [Call.Factory].
     * It should be a Singleton
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): Call.Factory {
        return OkHttpClient.Builder()
            .addNetworkInterceptor(HttpRequestInterceptor())
            .build()
    }

    /**
     * Provides an instance of Retrofit.
     * It should be a Singleton
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: dagger.Lazy<Call.Factory>): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            // Lazy-loading the OkHttpClient to prevent initializing OkHttp on the main thread
            .callFactory {
                okHttpClient.get().newCall(it)
            }
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    /**
     * Provides an instance of GithubService.
     * It should be a Singleton
     */
    @Provides
    @Singleton
    fun providePexelsService(retrofit: Retrofit): PexelsService =
        retrofit.create(PexelsService::class.java)


    /**
     * Provides an instance of GithubClient.
     * It should be a Singleton
     */
    @Provides
    @Singleton
    fun providePexelsClient(pexelsService: PexelsService): PexelsClient =
        PexelsClient(pexelsService)
}