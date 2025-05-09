package com.vanluong.search

import com.vanluong.domain.FetchCuratedPhotoUseCase
import com.vanluong.domain.SaveRecentPhotoUseCase
import com.vanluong.domain.SearchPhotoUseCase
import com.vanluong.search.main.PhotoSearchViewModel
import com.vanluong.testing.MainCoroutinesRule
import com.vanluong.testing.MockDataUtil
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
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
class PhotoSearchViewModelTest {
    private lateinit var viewModel: PhotoSearchViewModel
    private val searchPhotoUseCase: SearchPhotoUseCase = mock()
    private val curatedPhotoUseCase: FetchCuratedPhotoUseCase = mock()
    private val saveRecentPhotoUseCase: SaveRecentPhotoUseCase = mock()

    @get:Rule
    val mainCoroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        viewModel = PhotoSearchViewModel(
            searchPhotoUseCase,
            curatedPhotoUseCase,
            saveRecentPhotoUseCase
        )
    }

    @Test
    fun saveRecentPhoto() = runTest {
        val mockData = MockDataUtil.mockPhotoList()[0]

        // Mocking the use cases to return empty flows to avoid exception when use cachedIn
        whenever(searchPhotoUseCase("")).thenReturn(flowOf())
        whenever(curatedPhotoUseCase()).thenReturn(flowOf())

        viewModel.saveRecentPhoto(mockData)
        advanceUntilIdle() // Wait for all coroutines to finish
        verify(saveRecentPhotoUseCase).invoke(mockData)
    }
}