package com.vanluong.network.service

import com.vanluong.network.model.NetworkSearchResult
import retrofit2.http.GET
import retrofit2.http.Path
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
    @GET("search/{query}")
    suspend fun searchImage(
        @Path("query") query: String,
        @Query("per_page") perPage: Int
    ): Result<NetworkSearchResult>
}