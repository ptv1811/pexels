package com.vanluong.data.di

import com.vanluong.data.repository.search.SearchRepository
import com.vanluong.data.repository.search.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by van.luong
 * on 07,May,2025
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideSearchRepository(impl: SearchRepositoryImpl): SearchRepository = impl
}