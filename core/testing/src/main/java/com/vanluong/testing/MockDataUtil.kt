package com.vanluong.testing

import com.vanluong.model.Photo
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

    fun mockPhotoList(): List<Photo> {
        return listOf(
            Photo(
                id = 1,
                width = 100,
                height = 200,
                url = "https://example.com/photo1.jpg",
                photographer = "John Doe",
                alt = "Beautiful scenery",
            ),
            Photo(
                id = 2,
                width = 200,
                height = 400,
                url = "https://example.com/photo2.jpg",
                photographer = "Jane Smith",
                alt = "Hello world",
            ),
        )
    }
}