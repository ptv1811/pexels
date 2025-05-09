package com.vanluong.domain

import com.vanluong.data.repository.photo.PhotoRepository
import com.vanluong.model.Photo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by van.luong
 * on 08,May,2025
 */
class GetRecentPhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke(id: Long): Flow<Photo?> {
        return photoRepository.getPhotoById(id)
    }
}