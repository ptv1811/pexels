package com.vanluong.data.repository.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vanluong.model.Photo
import com.vanluong.model.Resource
import com.vanluong.network.model.toModel
import javax.inject.Inject

/**
 * Created by van.luong
 * on 07,May,2025
 */
private const val STARTING_PAGE = 1
const val NETWORK_PAGE_SIZE = 20

class PhotoPagingSource @Inject constructor(
    private val searchRepository: SearchRepository,
    private val query: String
) : PagingSource<Int, Photo>() {

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: STARTING_PAGE
        return try {
            var loadResult: LoadResult<Int, Photo>? = null

            // Collect the flow from the repository
            searchRepository.searchPhotos(query, position, NETWORK_PAGE_SIZE).collect { response ->
                loadResult = when (response) {
                    is Resource.Success -> {
                        val photos = response.body.photos.map { it.toModel() }
                        val totalResults = response.body.totalResults

                        val nextKey =
                            if (NETWORK_PAGE_SIZE * position < totalResults) position + 1 else null
                        LoadResult.Page(
                            data = photos,
                            prevKey = if (position == STARTING_PAGE) null else position - 1,
                            nextKey = nextKey
                        )
                    }

                    is Resource.ServerError -> {
                        LoadResult.Error(Exception("Server error: ${response.message}"))
                    }

                    is Resource.DataError -> {
                        LoadResult.Error(response.error)
                    }

                    else -> {
                        LoadResult.Error(Exception("Unexpected response"))
                    }
                }
            }

            loadResult ?: LoadResult.Error(Exception("No response received"))
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

}