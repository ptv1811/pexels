package com.vanluong.network.adapters

import com.vanluong.model.NetworkResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse

/**
 * Created by van.luong
 * on 06,May,2025
 */
class NetworkResponseCall<T : Any>(
    private val delegate: Call<T>,
    private val coroutineScope: CoroutineScope
) : Call<NetworkResponse<T>> {

    override fun enqueue(callback: Callback<NetworkResponse<T>>) {
        coroutineScope.launch {
            try {
                val response = delegate.awaitResponse()
                val result = if (response.isSuccessful) {
                    NetworkResponse.Success(response.body()!!)
                } else {
                    NetworkResponse.ServerError(response.code(), response.message())
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(result))

            } catch (e: Exception) {
                val result = NetworkResponse.NetworkError(e)
                callback.onResponse(this@NetworkResponseCall, Response.success(result))
            }
        }
    }

    override fun execute(): Response<NetworkResponse<T>> =
        runBlocking(coroutineScope.coroutineContext) {
            try {
                val response = delegate.execute()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Response.success(NetworkResponse.Success(body))
                    } else {
                        Response.success(
                            NetworkResponse.ServerError(
                                response.code(),
                                "Empty response body"
                            )
                        )
                    }
                } else {
                    Response.success(
                        NetworkResponse.ServerError(
                            response.code(),
                            response.message()
                        )
                    )
                }
            } catch (e: Exception) {
                Response.success(NetworkResponse.NetworkError(e))
            }
        }

    override fun clone(): Call<NetworkResponse<T>> =
        NetworkResponseCall(delegate.clone(), coroutineScope)

    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun isCanceled(): Boolean = delegate.isCanceled
    override fun cancel() = delegate.cancel()
}