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
    @field:Json(name = "photographer") val photographer: String,
    @field:Json(name = "src") val src: Src,
    @field:Json(name = "alt") val alt: String,
)

@JsonClass(generateAdapter = true)
data class Src(
    @field:Json(name = "original") val original: String,
)

/**
 * Extension function to convert [NetworkPhoto] to [Photo]
 */
fun NetworkPhoto.toModel(): Photo = Photo(
    id = this.id,
    width = this.width,
    height = this.height,
    url = this.src.original,
    photographer = this.photographer,
    alt = this.alt
)

fun List<NetworkPhoto>.toModelList(): List<Photo> {
    return this.map { it.toModel() }
}

fun List<Photo>.toNetworkList(): List<NetworkPhoto> {
    return this.map {
        NetworkPhoto(
            it.id,
            it.width,
            it.height,
            it.url,
            it.photographer,
            Src(original = it.url),
            it.alt
        )
    }
}