package com.vanluong.network.service

import com.vanluong.network.model.NetworkPhoto
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
    suspend fun searchImages(query: String, perPage: Int = 15): List<NetworkPhoto> =
        pexelsService.searchImage(query, perPage)
}