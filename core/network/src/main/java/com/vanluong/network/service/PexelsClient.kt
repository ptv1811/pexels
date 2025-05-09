package com.vanluong.network.service

import com.vanluong.model.Resource
import com.vanluong.network.model.NetworkPhoto
import com.vanluong.network.model.NetworkSearchResult
import javax.inject.Inject

/**
 * This class is responsible for making network requests to the Pexels API.
 */
class PexelsClient @Inject constructor(
    private val pexelsService: PexelsService
) {
    /**
     * Fetches a paged list of photos from the API based on user input.
     *
     * @param query The user input.
     * @param perPage The number of photos to return per page. Default is 15.
     * @return A List containing a list of [NetworkPhoto] objects.
     */
    suspend fun searchImages(
        query: String,
        page: Int,
        perPage: Int = 15
    ): Resource<NetworkSearchResult> =
        pexelsService.searchImage(query, page, perPage)

    /**
     * Fetches a list of curated photos from the API.
     */
    suspend fun fetchCuratedPhotos(
    ): Resource<NetworkSearchResult> =
        pexelsService.fetchCuratedPhotos()
}