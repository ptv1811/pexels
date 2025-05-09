package com.vanluong.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.vanluong.data.repository.photo.PhotoRepository
import com.vanluong.data.repository.photo.RecentPhotoPagingSource
import com.vanluong.data.repository.search.NETWORK_PAGE_SIZE
import javax.inject.Inject

/**
 * Created by van.luong
 * on 09,May,2025
 */
class GetAllRecentPhotosUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
) {
    operator fun invoke() = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false,
            maxSize = 120,
        ),
        pagingSourceFactory = { RecentPhotoPagingSource(photoRepository) }
    ).flow
}