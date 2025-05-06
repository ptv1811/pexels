package com.vanluong.model.exception

import com.vanluong.model.http.StatusCode

/**
 * Created by van.luong
 * on 06,May,2025
 */
// Custom exception for HTTP errors
class HttpException(val code: StatusCode, message: String) : Exception(message)

// Custom exception for empty photo results
class EmptyPhotoException(message: String = "No photos found for the given query.") :
    Exception(message)

// Custom exception for network connectivity issues
class NetworkUnavailableException(message: String = "Network is unavailable") : Exception(message)