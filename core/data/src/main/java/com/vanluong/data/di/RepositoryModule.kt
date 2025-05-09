package com.vanluong.data.di

import com.vanluong.data.repository.photo.PhotoRepository
import com.vanluong.data.repository.photo.PhotoRepositoryImpl
import com.vanluong.data.repository.search.SearchRepository
import com.vanluong.data.repository.search.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by van.luong
 * on 07,May,2025
 *
 * This Hilt module provides the dependencies for the repositories in the data layer.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /**
     * Provides an instance of [SearchRepository] using Dagger Hilt.
     *
     * @param impl The implementation of [SearchRepository].
     * @return An instance of [SearchRepository].
     */
    @Provides
    fun provideSearchRepository(impl: SearchRepositoryImpl): SearchRepository = impl

    /**
     * Provides an instance of [PhotoRepository] using Dagger Hilt.
     *
     * @param impl The implementation of [PhotoRepository].
     * @return An instance of [PhotoRepository].
     */
    @Provides
    fun provideDetailsRepository(impl: PhotoRepositoryImpl): PhotoRepository = impl
}