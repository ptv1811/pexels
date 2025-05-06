package com.vanluong.database.di

import android.content.Context
import androidx.room.Room
import com.vanluong.database.PexelsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by van.luong
 * on 06,May,2025
 *
 * This Hilt module provides the dependencies for the Pexels database.
 */
@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    /**
     * Provides a singleton instance of [PexelsDatabase] using Room.
     *
     * @param context The application context.
     */
    @Provides
    @Singleton
    fun providePexelsDatabase(
        @ApplicationContext context: Context
    ): PexelsDatabase = Room.databaseBuilder(
        context,
        PexelsDatabase::class.java,
        "pexels.db"
    ).build()
}