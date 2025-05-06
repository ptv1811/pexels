package com.vanluong.network.adapters

import com.vanluong.model.Resource
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
) : Call<Resource<T>> {

    override fun enqueue(callback: Callback<Resource<T>>) {
        coroutineScope.launch {
            try {
                val response = delegate.awaitResponse()
                val result = if (response.isSuccessful) {
                    Resource.Success(response.body()!!)
                } else {
                    Resource.ServerError(response.code(), response.message())
                }
                callback.onResponse(this@NetworkResponseCall, Response.success(result))

            } catch (e: Exception) {
                val result = Resource.DataError(e)
                callback.onResponse(this@NetworkResponseCall, Response.success(result))
            }
        }
    }

    override fun execute(): Response<Resource<T>> =
        runBlocking(coroutineScope.coroutineContext) {
            try {
                val response = delegate.execute()
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        Response.success(Resource.Success(body))
                    } else {
                        Response.success(
                            Resource.ServerError(
                                response.code(),
                                "Empty response body"
                            )
                        )
                    }
                } else {
                    Response.success(
                        Resource.ServerError(
                            response.code(),
                            response.message()
                        )
                    )
                }
            } catch (e: Exception) {
                Response.success(Resource.DataError(e))
            }
        }

    override fun clone(): Call<Resource<T>> =
        NetworkResponseCall(delegate.clone(), coroutineScope)

    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun isCanceled(): Boolean = delegate.isCanceled
    override fun cancel() = delegate.cancel()
}