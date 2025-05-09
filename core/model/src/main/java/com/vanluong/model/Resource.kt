package com.vanluong.model

import com.vanluong.model.http.ApiError

/**
 * Created by van.luong
 * on 06,May,2025
 */
sealed class Resource<out T> {
    data object Loading : Resource<Nothing>()
    data class Success<T>(val body: T) : Resource<T>()
    data class DataError(val error: Throwable) : Resource<Nothing>()
    data class ServerError(val error: ApiError) : Resource<Nothing>()
}