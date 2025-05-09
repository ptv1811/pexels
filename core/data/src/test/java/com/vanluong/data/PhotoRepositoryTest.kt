package com.vanluong.data

import app.cash.turbine.test
import com.vanluong.data.repository.photo.PhotoRepository
import com.vanluong.data.repository.photo.PhotoRepositoryImpl
import com.vanluong.database.dao.RecentPhotosDao
import com.vanluong.database.entity.toEntity
import com.vanluong.testing.MainCoroutinesRule
import com.vanluong.testing.MockDataUtil
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Created by van.luong
 * on 09,May,2025
 */
class PhotoRepositoryTest {
    private lateinit var photoRepository: PhotoRepository
    private var recentPhotosDao: RecentPhotosDao = mock()

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        photoRepository = PhotoRepositoryImpl(recentPhotosDao)
    }

    @Test
    fun cachePhotoTest() = runTest {
        val photo = MockDataUtil.mockPhotoList()[0]
        val photoEntity = photo.toEntity()
        photoRepository.cachePhoto(photo)

        verify(recentPhotosDao).insert(photoEntity)
    }

    @Test
    fun getPhotoByIdTest() = runTest {
        val photoEntity = MockDataUtil.mockPhotoList()[0].toEntity()
        whenever(recentPhotosDao.getPhotoById(1L)).thenReturn(photoEntity)

        photoRepository.getPhotoById(1L).test {
            val item = awaitItem()
            assert(item != null)
            if (item != null) {
                assert(item.id == photoEntity.id)
                assert(item.url == photoEntity.url)
                assert(item.alt == photoEntity.alt)
            }

            awaitComplete()
        }
    }

    @Test
    fun getAllPhotosTest() = runTest {
        val photoEntityList = MockDataUtil.mockPhotoList().map { it.toEntity() }
        whenever(recentPhotosDao.getAllPhotos()).thenReturn(photoEntityList)

        photoRepository.getRecentPhotos().test {
            val item = awaitItem()
            assert(item.isNotEmpty())
            assert(item.size == photoEntityList.size)

            item.forEachIndexed { index, photo ->
                assert(photo.id == photoEntityList[index].id)
                assert(photo.url == photoEntityList[index].url)
                assert(photo.alt == photoEntityList[index].alt)
            }

            awaitComplete()
        }
    }
}