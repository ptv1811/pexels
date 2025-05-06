package com.vanluong.network.adapters

import kotlinx.coroutines.CoroutineScope
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

/**
 * Created by van.luong
 * on 06,May,2025
 */
internal class ResultCallAdapter(
    private val resultType: Type,
    private val coroutineScope: CoroutineScope,
) : CallAdapter<Type, Call<Result<Type?>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): Call<Result<Type?>> {
        return ResultCall(call, coroutineScope)
    }
}