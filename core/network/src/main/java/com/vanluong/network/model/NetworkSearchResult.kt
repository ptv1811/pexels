package com.vanluong.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Created by van.luong
 * on 06,May,2025
 */
@JsonClass(generateAdapter = true)
data class NetworkSearchResult(
    @field:Json(name = "page") val page: Int,
    @field:Json(name = "per_page") val perPage: Int,
    @field:Json(name = "photos") val photos: List<NetworkPhoto>,
    @field:Json(name = "total_results") val totalResults: Int,
    @field:Json(name = "next_page") val nextPage: String,
)