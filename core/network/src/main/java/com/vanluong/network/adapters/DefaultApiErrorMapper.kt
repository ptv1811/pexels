package com.vanluong.network.adapters

import com.vanluong.model.http.ApiError
import com.vanluong.model.http.ApiErrorMapper
import retrofit2.HttpException
import java.io.IOException

/**
 * Created by van.luong
 * on 09,May,2025
 */
class DefaultApiErrorMapper : ApiErrorMapper {
    override fun mapApiError(e: Throwable): ApiError {
        return when (e) {
            is IOException -> ApiError.Network("Network Connection issue")
            is HttpException -> {
                val code = e.code()
                val msg = e.message
                return when (code) {
                    in 400..499 -> ApiError.Client("Client error: $msg", code)
                    in 500..599 -> ApiError.Server("Server error: $msg", code)
                    else -> ApiError.Unexpected("Unexpected error: $msg")
                }
            }

            else -> ApiError.Unexpected("Unexpected error: ${e.localizedMessage}")
        }
    }
}