package com.vanluong.testing

import com.vanluong.model.RecentSearchQuery
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

/**
 * Created by van.luong
 * on 06,May,2025
 */
object MockDataUtil {
    fun mockRecentSearchQueryList(): List<RecentSearchQuery> {
        return listOf(
            RecentSearchQuery(
                query = "Android",
                queriedDate = Clock.System.now()
            ),
            RecentSearchQuery(
                query = "Kotlin",
                queriedDate = Clock.System.now().plus(1.hours)
            ),
            RecentSearchQuery(
                query = "RecyclerView",
                queriedDate = Clock.System.now().plus(2.hours)
            )
        )
    }
}