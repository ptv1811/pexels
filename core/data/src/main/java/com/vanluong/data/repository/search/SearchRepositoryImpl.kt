package com.vanluong.data.repository.search

import com.vanluong.database.dao.RecentSearchQueryDao
import com.vanluong.database.entity.RecentSearchQueryEntity
import com.vanluong.model.Photo
import com.vanluong.model.Resource
import com.vanluong.model.exception.EmptyPhotoException
import com.vanluong.network.model.toModel
import com.vanluong.network.service.PexelsClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.time.Clock

/**
 * Created by van.luong
 * on 06,May,2025
 */
class SearchRepositoryImpl @Inject constructor(
    private val pexelsClient: PexelsClient,
    private val pexelsDao: RecentSearchQueryDao,
) : SearchRepository {
    override suspend fun searchPhotos(
        query: String,
        page: Int,
        perPage: Int
    ): Flow<Resource<List<Photo>>> =
        flow {
            pexelsDao.insertOrReplaceRecentSearchQuery(
                RecentSearchQueryEntity(
                    query = query,
                    queriedDate = Clock.System.now()
                )
            )

            when (val response = pexelsClient.searchImages(query, page, perPage)) {
                is Resource.Success -> {
                    val photos = response.body.photos.map { it.toModel() }
                    if (photos.isEmpty()) {
                        emit(Resource.DataError(EmptyPhotoException()))
                        return@flow
                    }
                    emit(Resource.Success(photos))
                }

                is Resource.DataError -> {
                    emit(Resource.DataError(response.error))
                }

                is Resource.ServerError -> {
                    emit(Resource.ServerError(response.code, response.message))
                }

                Resource.Loading -> {
                    // No need to emit anything for loading state
                }
            }

        }.flowOn(Dispatchers.IO)
}