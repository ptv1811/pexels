package com.vanluong.data.repository.photo

import com.vanluong.model.Photo
import kotlinx.coroutines.flow.Flow

/**
 * Created by van.luong
 * on 08,May,2025
 */
interface PhotoRepository {
    suspend fun cachePhoto(photo: Photo)

    suspend fun getPhotoById(id: String): Flow<Photo?>
}