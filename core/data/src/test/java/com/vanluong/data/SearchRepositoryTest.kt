package com.vanluong.data

import app.cash.turbine.test
import com.vanluong.data.repository.search.SearchRepository
import com.vanluong.data.repository.search.SearchRepositoryImpl
import com.vanluong.database.dao.RecentSearchQueryDao
import com.vanluong.model.Resource
import com.vanluong.model.exception.EmptyPhotoException
import com.vanluong.network.model.NetworkSearchResult
import com.vanluong.network.model.toNetworkList
import com.vanluong.network.service.PexelsClient
import com.vanluong.network.service.PexelsService
import com.vanluong.testing.MainCoroutinesRule
import com.vanluong.testing.MockDataUtil
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever

/**
 * Created by van.luong
 * on 06,May,2025
 */
class SearchRepositoryTest {
    private lateinit var searchRepository: SearchRepository
    private lateinit var pexelsClient: PexelsClient

    private var pexelsService: PexelsService = mock()
    private var recentSearchQueryDao: RecentSearchQueryDao = mock()

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        pexelsClient = PexelsClient(pexelsService)
        searchRepository = SearchRepositoryImpl(pexelsClient, recentSearchQueryDao)
    }

    @Test
    fun searchPhotosTest() = runTest {
        val mockNetworkPhotoList = MockDataUtil.mockPhotoList().toNetworkList()
        val mockNetworkSearchResult = NetworkSearchResult(
            page = 1,
            perPage = 10,
            totalResults = 100,
            photos = mockNetworkPhotoList,
            nextPage = "https://api.pexels.com/v1/search?page=3&per_page=3&query=nature"
        )

        whenever(pexelsService.searchImage("nature", 1, 10))
            .thenReturn(Resource.Success(mockNetworkSearchResult))

        searchRepository.searchPhotos("nature", 1, 10).test {
            val expectItem = awaitItem()
            assert(expectItem is Resource.Success)
            val expectBody = (expectItem as Resource.Success).body

            assert(expectBody.size == 2)
            assert(expectBody[0].id == 1L)
            assert(expectBody[0].width == 100)
            assert(expectBody[0].height == 200)
            assert(expectBody[0].url == "https://example.com/photo1.jpg")
            assert(expectBody[0].photographer == "John Doe")
            awaitComplete()
        }
    }

    @Test
    fun searchPhotosExceptionTest() = runTest {
        whenever(pexelsService.searchImage("nature", 1, 10))
            .thenReturn(Resource.DataError(EmptyPhotoException()))

        searchRepository.searchPhotos("nature", 1, 10).test {
            val expectItem = awaitItem()
            assert(expectItem is Resource.DataError)
            val expectBody = (expectItem as Resource.DataError).error

            assert(expectBody is EmptyPhotoException)
            awaitComplete()
        }
    }
}