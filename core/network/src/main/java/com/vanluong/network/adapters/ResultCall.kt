package com.vanluong.network.adapters

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
class ResultCall<T : Any>(
    val delegate: Call<T>,
    private val coroutineScope: CoroutineScope
) : Call<Result<T?>> {
    override fun enqueue(callback: Callback<Result<T?>>) {
        coroutineScope.launch {
            try {
                val response = delegate.awaitResponse()
                val result = Result.success(response.body())
                callback.onResponse(this@ResultCall, Response.success(result))

            } catch (e: Exception) {
                val result = Result.failure<T>(e)
                callback.onResponse(this@ResultCall, Response.success(result))
            }
        }
    }

    override fun execute(): Response<Result<T?>> =
        runBlocking(coroutineScope.coroutineContext) {
            Response.success(Result.success(delegate.execute().body()))
        }

    override fun clone(): Call<Result<T?>> = ResultCall(delegate.clone(), coroutineScope)
    override fun request(): Request = delegate.request()
    override fun timeout(): Timeout = delegate.timeout()
    override fun isExecuted(): Boolean = delegate.isExecuted
    override fun isCanceled(): Boolean = delegate.isCanceled
    override fun cancel() = delegate.cancel()
}