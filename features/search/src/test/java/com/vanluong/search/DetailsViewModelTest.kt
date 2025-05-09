package com.vanluong.search

import app.cash.turbine.test
import com.vanluong.domain.GetRecentPhotoUseCase
import com.vanluong.search.details.DetailsViewModel
import com.vanluong.testing.MainCoroutinesRule
import com.vanluong.testing.MockDataUtil
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

/**
 * Created by van.luong
 * on 09,May,2025
 */
class DetailsViewModelTest {
    private lateinit var detailsViewModel: DetailsViewModel
    private var getRecentPhotoUseCase: GetRecentPhotoUseCase = mock()

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        detailsViewModel = DetailsViewModel(getRecentPhotoUseCase)
    }

    @Test
    fun getRecentPhotoTestFromUseCase() = runTest {
        val mockData = MockDataUtil.mockPhotoList()[0]
        whenever(getRecentPhotoUseCase(mockData.id)).thenReturn(
            flowOf(mockData)
        )

        detailsViewModel.getRecentPhoto(mockData.id)

        detailsViewModel.selectedPhotoStateFlow.test {
            // Initial state is null
            assertEquals(null, awaitItem())

            val successItem = awaitItem()
            assert(successItem != null)
            if (successItem != null) {
                assertEquals(successItem, mockData)
            }
        }
    }
}