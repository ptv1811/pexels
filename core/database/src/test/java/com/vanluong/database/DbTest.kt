package com.vanluong.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.vanluong.database.dao.RecentPhotosDao
import com.vanluong.database.dao.RecentSearchQueryDao
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

/**
 * Created by van.luong
 * on 09,May,2025
 */
@RunWith(RobolectricTestRunner::class)
internal abstract class DbTest {
    private lateinit var db: PexelsDatabase
    protected lateinit var searchDao: RecentSearchQueryDao
    protected lateinit var photoDao: RecentPhotosDao

    @Before
    fun initDB() {

        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, PexelsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        searchDao = db.recentSearchQueryDao()
        photoDao = db.recentPhotosDao()
    }

    @After
    fun closeDB() {
        db.close()
    }
}