package com.vanluong.model

/**
 * Created by van.luong
 * on 06,May,2025
 */
sealed class NetworkResponse<out T> {
    data class Success<T>(val body: T) : NetworkResponse<T>()
    data class NetworkError(val error: Throwable) : NetworkResponse<Nothing>()
    data class ServerError(val code: Int, val message: String?) : NetworkResponse<Nothing>()
}