package com.vanluong.database

import com.vanluong.database.entity.RecentSearchQueryEntity
import com.vanluong.testing.MockDataUtil
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import kotlin.time.Clock

/**
 * Created by van.luong
 * on 06,May,2025
 */
internal class RecentSearchDaoTest : DbTest() {

    @Test
    fun insertOrUpdateRecentSearchQueryTest() = runTest {
        val query = MockDataUtil.mockRecentSearchQueryList()[0]
        searchDao.insertOrReplaceRecentSearchQuery(
            RecentSearchQueryEntity(
                query = query.query,
                queriedDate = Clock.System.now(),
            )
        )

        val fetchedQuery = searchDao.getRecentSearchQueryEntities(1).first()
        assert(fetchedQuery[0].query == "Android")
    }

    @Test
    fun fetchRecentSearchesTest() = runTest {
        val query = MockDataUtil.mockRecentSearchQueryList().map {
            RecentSearchQueryEntity(it.query, it.queriedDate)
        }
        query.forEach {
            searchDao.insertOrReplaceRecentSearchQuery(it)
        }

        val fetchedQueries = searchDao.getRecentSearchQueryEntities(5).first()
        assertThat(fetchedQueries.size, `is`(3))
        assertThat(fetchedQueries[0].query, `is`("RecyclerView"))
        assertThat(fetchedQueries[1].query, `is`("Kotlin"))
        assertThat(fetchedQueries[2].query, `is`("Android"))
    }

    @Test
    fun testClearRecentSearches() = runTest {
        val query = MockDataUtil.mockRecentSearchQueryList().map {
            RecentSearchQueryEntity(it.query, it.queriedDate)
        }
        query.forEach {
            searchDao.insertOrReplaceRecentSearchQuery(it)
        }

        searchDao.clearRecentSearchQueries()
        val fetchedQueries = searchDao.getRecentSearchQueryEntities(5).first()
        assertTrue(fetchedQueries.isEmpty())
    }
}