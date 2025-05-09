package com.vanluong.data.repository.photo

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.vanluong.model.Photo
import javax.inject.Inject

/**
 * Created by van.luong
 * on 09,May,2025
 */
private const val STARTING_PAGE = 1
const val PAGE_SIZE = 50

class RecentPhotoPagingSource @Inject constructor(
    private val photoRepository: PhotoRepository
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
            photoRepository.getRecentPhotos().collect { photos ->
                val totalResults = photos.size

                val nextKey =
                    if (PAGE_SIZE * position < totalResults) position + 1 else null
                loadResult = LoadResult.Page(
                    data = photos,
                    prevKey = if (position == STARTING_PAGE) null else position - 1,
                    nextKey = nextKey
                )
            }

            loadResult ?: LoadResult.Error(Exception("No response received"))
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

}