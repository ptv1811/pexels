package com.vanluong.network.adapters

import com.vanluong.model.NetworkResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by van.luong
 * on 06,May,2025
 */
class NetworkResponseCallFactory private constructor(
    private val coroutineScope: CoroutineScope
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) {
            return null
        }

        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != NetworkResponse::class.java) {
            return null
        }

        val responseType = getParameterUpperBound(0, callType as ParameterizedType)
        return NetworkResponseAdapter<Any>(responseType, coroutineScope)
    }

    companion object {
        fun create(
            coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO)
        ): NetworkResponseCallFactory = NetworkResponseCallFactory(coroutineScope)
    }
}