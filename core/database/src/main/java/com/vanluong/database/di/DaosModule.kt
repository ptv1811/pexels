package com.vanluong.database.di

import com.vanluong.database.PexelsDatabase
import com.vanluong.database.dao.RecentSearchQueryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by van.luong
 * on 06,May,2025
 *
 * This Hilt module provides the dependencies for the DAOs in the Pexels database.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    /**
     * Provides an instance of [PexelsDatabase] using Room.
     *
     * @param database The [PexelsDatabase] instance.
     * @return An instance of [RecentSearchQueryDao].
     */
    @Provides
    fun provideRecentSearchQueryDao(
        database: PexelsDatabase,
    ): RecentSearchQueryDao {
        return database.recentSearchQueryDao()
    }
}