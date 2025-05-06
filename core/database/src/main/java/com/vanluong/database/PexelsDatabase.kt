package com.vanluong.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.vanluong.database.dao.RecentSearchQueryDao
import com.vanluong.database.entity.RecentSearchQueryEntity
import com.vanluong.database.util.InstantConverter

/**
 * Created by van.luong
 * on 06,May,2025
 */
@Database(
    entities = [RecentSearchQueryEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    InstantConverter::class,
)
abstract class PexelsDatabase : RoomDatabase() {
    abstract fun recentSearchQueryDao(): RecentSearchQueryDao
}