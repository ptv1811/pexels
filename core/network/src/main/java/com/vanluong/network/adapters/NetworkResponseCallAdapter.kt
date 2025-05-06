package com.vanluong.network.adapters

import com.vanluong.model.NetworkResponse
import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * Created by van.luong
 * on 06,May,2025
 */
class NetworkResponseAdapter<T : Any>(
    private val responseType: Type,
    private val coroutineScope: CoroutineScope
) : CallAdapter<T, Call<NetworkResponse<T>>> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<T>): Call<NetworkResponse<T>> {
        return NetworkResponseCall(call, coroutineScope)
    }
}