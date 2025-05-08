package com.vanluong.data.repository.search

import com.vanluong.model.Resource
import com.vanluong.model.Photo
import com.vanluong.network.model.NetworkSearchResult
import kotlinx.coroutines.flow.Flow

/**
 * Created by van.luong
 * on 06,May,2025
 */
interface SearchRepository {
    suspend fun searchPhotos(query: String, page: Int, perPage: Int): Flow<Resource<NetworkSearchResult>>

    suspend fun fetchCuratedPhotos(): Flow<Resource<List<Photo>>>
}