package com.vanluong.model

/**
 * Created by van.luong
 * on 06,May,2025
 */
sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<T>(val body: T) : Resource<T>()
    data class DataError(val error: Throwable) : Resource<Nothing>()
    data class ServerError(val code: Int, val message: String?) : Resource<Nothing>()
}