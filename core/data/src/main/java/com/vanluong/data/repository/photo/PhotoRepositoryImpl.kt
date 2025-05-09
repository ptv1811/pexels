package com.vanluong.data.repository.photo

import com.vanluong.database.dao.RecentPhotosDao
import com.vanluong.database.entity.toDomain
import com.vanluong.database.entity.toDomainList
import com.vanluong.database.entity.toEntity
import com.vanluong.model.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Created by van.luong
 * on 08,May,2025
 */
class PhotoRepositoryImpl @Inject constructor(
    private val recentPhotosDao: RecentPhotosDao,
) : PhotoRepository {
    override suspend fun cachePhoto(photo: Photo) {
        recentPhotosDao.insert(photo.toEntity())
        recentPhotosDao.enforceLRULimit()
    }

    override suspend fun getPhotoById(id: Long): Flow<Photo?> = flow {
        emit(recentPhotosDao.getPhotoById(id)?.toDomain())
    }.flowOn(Dispatchers.IO)

    override suspend fun getRecentPhotos(): Flow<List<Photo>> = flow {
        emit(recentPhotosDao.getAllPhotos().toDomainList())
    }.flowOn(Dispatchers.IO)
}