package com.vanluong.database

import com.vanluong.database.entity.PhotoEntity
import com.vanluong.database.entity.toEntity
import com.vanluong.testing.MockDataUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * Created by van.luong
 * on 09,May,2025
 */
internal class RecentPhotoTest : DbTest() {
    @Test
    fun insertAndRetrievePhotoTest() = runTest {
        val photo = MockDataUtil.mockPhotoList()[0].toEntity()
        photoDao.insert(photo)

        val retrievedPhoto = photoDao.getPhotoById("1")
        assert(retrievedPhoto?.id == 1L)
        assert(retrievedPhoto?.url == "https://example.com/photo1.jpg")
    }

    @Test
    fun retrieveAllPhotosTest() = runTest {
        val mockPhotos = listOf(
            PhotoEntity(
                id = 1L,
                width = 100,
                height = 200,
                url = "https://example.com/photo1.jpg",
                photographer = "John Doe",
                alt = "Beautiful scenery",
                timestamp = System.currentTimeMillis()
            ),
            PhotoEntity(
                id = 2L,
                width = 200,
                height = 400,
                url = "https://example.com/photo2.jpg",
                photographer = "Jane Smith",
                alt = "Hello world",
                timestamp = System.currentTimeMillis() + 1000 // Simulate different timestamps
            )
        )
        mockPhotos.forEach {
            photoDao.insert(it)
            delay(1000) // Simulate time delay for different timestamps
        }

        val photos = photoDao.getAllPhotos()
        assert(photos.size == 2)
        assert(photos[0].id == 2L) // Ordered by timestamp DESC
        assert(photos[1].id == 1L)
    }

    @Test
    fun enforceLRULimitTest() = runTest {
        for (i in 1..55) {
            photoDao.insert(
                PhotoEntity(
                    id = i.toLong(),
                    width = 100,
                    height = 200,
                    url = "https://example.com/photo$i.jpg",
                    photographer = "John Doe",
                    alt = "Hello",
                    timestamp = System.currentTimeMillis() + i
                )
            )
        }

        photoDao.enforceLRULimit()
        val photos = photoDao.getAllPhotos()
        assert(photos.size == 50) // Limit enforced
        assert(photos[0].id == 55L) // Most recent photo
        assert(photos.last().id == 6L) // Oldest within limit
    }
}