package com.vanluong.data.repository.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vanluong.model.Photo
import com.vanluong.model.Resource
import javax.inject.Inject

/**
 * Created by van.luong
 * on 07,May,2025
 */
private const val STARTING_PAGE = 1

class CuratedPhotoPagingSource @Inject constructor(
    private val searchRepository: SearchRepository
) : PagingSource<Int, Photo>(
) {
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
            searchRepository.fetchCuratedPhotos().collect { response ->
                loadResult = when (response) {
                    is Resource.Success -> {
                        val photos = response.body
                        val nextKey =
                            if (NETWORK_PAGE_SIZE * position < photos.size) position + (params.loadSize / NETWORK_PAGE_SIZE) else null
                        LoadResult.Page(
                            data = photos,
                            prevKey = if (position == STARTING_PAGE) null else position - 1,
                            nextKey = nextKey
                        )
                    }

                    is Resource.ServerError -> {
                        LoadResult.Error(Exception("Server error: ${response.error.message}"))
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