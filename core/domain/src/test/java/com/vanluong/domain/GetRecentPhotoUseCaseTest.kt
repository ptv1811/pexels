package com.vanluong.domain

import app.cash.turbine.test
import com.vanluong.data.repository.photo.PhotoRepository
import com.vanluong.testing.MockDataUtil
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

/**
 * Created by van.luong
 * on 09,May,2025
 */
class GetRecentPhotoUseCaseTest {
    private val photoRepository: PhotoRepository = mock()

    @Test
    fun testInvokeShouldReturnPhoto() = runTest {
        val mockPhoto = MockDataUtil.mockPhotoList()[0]
        val mockId = 1L

        whenever(photoRepository.getPhotoById(mockId))
            .thenReturn(flowOf(mockPhoto))

        val useCase = GetRecentPhotoUseCase(photoRepository)
        val result = useCase(mockId)

        result.test {
            val initialItem = awaitItem()
            assert(initialItem == mockPhoto)
        }
    }
}