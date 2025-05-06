package com.vanluong.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vanluong.model.RecentSearchQuery
import kotlin.time.Instant

/**
 * Created by van.luong
 * on 06,May,2025
 *
 * Database entity that stores recent search queries.
 */
@Entity(
    tableName = "recentSearchQuery",
)
class RecentSearchQueryEntity(
    @PrimaryKey
    val query: String,
    @ColumnInfo
    val queriedDate: Instant
)

fun RecentSearchQueryEntity.toModel(): RecentSearchQuery {
    return RecentSearchQuery(
        query = this.query,
        queriedDate = this.queriedDate
    )
}