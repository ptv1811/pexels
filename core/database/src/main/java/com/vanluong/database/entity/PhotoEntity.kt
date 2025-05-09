package com.vanluong.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.vanluong.model.Photo

/**
 * Created by van.luong
 * on 08,May,2025
 *
 * Entity class for Photo to store in the database.
 */
@Entity(tableName = "recentPhotos")
data class PhotoEntity(
    @PrimaryKey val id: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    val alt: String,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Extension functions to map between [Photo] and [PhotoEntity].
 * because they are in separated modules.
 */
fun PhotoEntity.toDomain(): Photo {
    return Photo(
        id = id,
        width = width,
        height = height,
        url = url,
        photographer = photographer,
        alt = alt
    )
}

fun Photo.toEntity(): PhotoEntity {
    return PhotoEntity(
        id = id,
        width = width,
        height = height,
        url = url,
        photographer = photographer,
        alt = alt
    )
}

fun List<PhotoEntity>.toDomainList(): List<Photo> {
    return map { it.toDomain() }
}

fun List<Photo>.toEntityList(): List<PhotoEntity> {
    return map { it.toEntity() }
}