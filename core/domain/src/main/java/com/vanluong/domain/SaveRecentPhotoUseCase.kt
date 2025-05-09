package com.vanluong.domain

import com.vanluong.data.repository.photo.PhotoRepository
import com.vanluong.model.Photo
import javax.inject.Inject

/**
 * Created by van.luong
 * on 08,May,2025
 */
class SaveRecentPhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository,
) {
    suspend operator fun invoke(photo: Photo) {
        photoRepository.cachePhoto(photo)
    }
}