package com.vanluong.model

data class Photo(
    val id: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String
)

data class TargetSize(val width: Int, val height: Int)

fun Photo.calculateScaledSize(screenWidth: Int, columns: Int): TargetSize {
    val targetWidth = screenWidth / columns
    val aspectRatio = height.toFloat() / width
    val targetHeight = (targetWidth * aspectRatio).toInt()
    return TargetSize(targetWidth, targetHeight)
}