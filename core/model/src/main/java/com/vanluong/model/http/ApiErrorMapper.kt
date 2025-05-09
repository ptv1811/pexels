package com.vanluong.model.http

/**
 * Created by van.luong
 * on 09,May,2025
 */
interface ApiErrorMapper {
    fun mapApiError(e: Throwable): ApiError
}