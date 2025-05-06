package com.vanluong.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.vanluong.database.entity.RecentSearchQueryEntity
import kotlinx.coroutines.flow.Flow

/**
 * Created by van.luong
 * on 06,May,2025
 *
 * DAO for [RecentSearchQueryEntity] access.
 */
@Dao
interface RecentSearchQueryDao {
    @Query(value = "SELECT * FROM recentSearchQuery ORDER BY queriedDate DESC LIMIT :limit")
    fun getRecentSearchQueryEntities(limit: Int): Flow<List<RecentSearchQueryEntity>>

    @Upsert
    suspend fun insertOrReplaceRecentSearchQuery(recentSearchQuery: RecentSearchQueryEntity)

    @Query(value = "DELETE FROM recentSearchQuery")
    suspend fun clearRecentSearchQueries()
}