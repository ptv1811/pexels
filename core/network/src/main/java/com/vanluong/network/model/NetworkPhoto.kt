package com.vanluong.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.vanluong.model.Photo

/**
 * Network representation of [Photo]
 */
@JsonClass(generateAdapter = true)
data class NetworkPhoto(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "width") val width: Int,
    @field:Json(name = "height") val height: Int,
    @field:Json(name = "url") val url: String,
    @field:Json(name = "photographer") val photographer: String
)

/**
 * Extension function to convert [NetworkPhoto] to [Photo]
 */
fun NetworkPhoto.toModel(): Photo = Photo(
    id = this.id,
    width = this.width,
    height = this.height,
    url = this.url,
    photographer = this.photographer
)