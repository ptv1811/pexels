package com.vanluong.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vanluong.data.repository.search.NETWORK_PAGE_SIZE
import com.vanluong.data.repository.search.PhotoPagingSource
import com.vanluong.data.repository.search.SearchRepository
import javax.inject.Inject

/**
 * Created by van.luong
 * on 07,May,2025
 *
 * This use case is responsible for searching photos.
 */
class SearchPhotoUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(query: String) = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false,
            maxSize = 120,
        ),
        pagingSourceFactory = { PhotoPagingSource(searchRepository, query) }
    ).flow
}