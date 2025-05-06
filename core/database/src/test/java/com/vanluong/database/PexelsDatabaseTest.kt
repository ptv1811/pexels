package com.vanluong.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.vanluong.database.dao.RecentSearchQueryDao
import com.vanluong.database.entity.RecentSearchQueryEntity
import com.vanluong.testing.MockDataUtil
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.time.Clock

/**
 * Created by van.luong
 * on 06,May,2025
 */
@RunWith(RobolectricTestRunner::class)
class PexelsDatabaseTest {
    private lateinit var db: PexelsDatabase
    private lateinit var dao: RecentSearchQueryDao

    @Before
    fun initDB() {

        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, PexelsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.recentSearchQueryDao()
    }

    @After
    fun closeDB() {
        db.close()
    }

    @Test
    fun insertOrUpdateRecentSearchQueryTest() = runTest {
        val query = MockDataUtil.mockRecentSearchQueryList()[0]
        dao.insertOrReplaceRecentSearchQuery(
            RecentSearchQueryEntity(
                query = query.query,
                queriedDate = Clock.System.now(),
            )
        )

        val fetchedQuery = dao.getRecentSearchQueryEntities(1).first()
        assert(fetchedQuery[0].query == "Android")
    }

    @Test
    fun fetchRecentSearchesTest() = runTest {
        val query = MockDataUtil.mockRecentSearchQueryList().map {
            RecentSearchQueryEntity(it.query, it.queriedDate)
        }
        query.forEach {
            dao.insertOrReplaceRecentSearchQuery(it)
        }

        val fetchedQueries = dao.getRecentSearchQueryEntities(5).first()
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
            dao.insertOrReplaceRecentSearchQuery(it)
        }

        dao.clearRecentSearchQueries()
        val fetchedQueries = dao.getRecentSearchQueryEntities(5).first()
        assertTrue(fetchedQueries.isEmpty())
    }
}