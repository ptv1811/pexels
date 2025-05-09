package com.vanluong.network.service

import com.vanluong.model.Resource
import com.vanluong.network.model.NetworkSearchResult
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This interface defines the Pexels API endpoints for searching images based on user input.
 */
interface PexelsService {
    /**
     * Get a list of photos from the Pexels API based on user input.
     * @param query The user input.
     * @param perPage The number of photos to return per page.
     */
    @GET("search")
    suspend fun searchImage(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Resource<NetworkSearchResult>

    /**
     * Get a list of curated photos from the Pexels API.
     * This helps when user opens app for the first time or just delete their searches.
     * so we display curated photos to them.
     *
     * @param perPage The number of photos to return per page.
     */
    @GET("curated")
    suspend fun fetchCuratedPhotos(
        @Query("per_page") perPage: Int = 20
    ): Resource<NetworkSearchResult>
}