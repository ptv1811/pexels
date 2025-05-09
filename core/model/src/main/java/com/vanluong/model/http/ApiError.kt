package com.vanluong.model.http

/**
 * Created by van.luong
 * on 06,May,2025
 */
sealed class ApiError(val message: String) {
    data class Unexpected(val msg: String) : ApiError(msg)
    data class Network(val msg: String) : ApiError(msg)
    data class Server(val msg: String, val code: Int) : ApiError(msg)
    data class Client(val msg: String, val code: Int) : ApiError(msg)
}

