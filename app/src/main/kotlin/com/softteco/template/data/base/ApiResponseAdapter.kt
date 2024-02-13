package com.softteco.template.data.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResultCallAdapter(
    private val resultType: Type,
    private val coroutineScope: CoroutineScope,
) : CallAdapter<Type, Call<ApiResult<Type>>> {

    override fun responseType(): Type = resultType

    override fun adapt(call: Call<Type>): Call<ApiResult<Type>> {
        return ApiResultCall(call, coroutineScope)
    }
}

class ApiResultCall<T : Any>(
    private val proxy: Call<T>,
    private val coroutineScope: CoroutineScope,
) : Call<ApiResult<T>> {

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        proxy.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                coroutineScope.launch {
                    val apiResult = handleApiResponse { response }
                    callback.onResponse(this@ApiResultCall, Response.success(apiResult))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val networkResult = ApiException<T>(t)
                callback.onResponse(this@ApiResultCall, Response.success(networkResult))
            }
        })
    }

    override fun execute(): Response<ApiResult<T>> = throw NotImplementedError()
    override fun clone(): Call<ApiResult<T>> = ApiResultCall(proxy.clone(), coroutineScope)
    override fun request(): Request = proxy.request()
    override fun timeout(): Timeout = proxy.timeout()
    override fun isExecuted(): Boolean = proxy.isExecuted
    override fun isCanceled(): Boolean = proxy.isCanceled
    override fun cancel() {
        proxy.cancel()
    }
}

class ApiResultCallAdapterFactory private constructor(
    private val coroutineScope: CoroutineScope
) : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
        return when {
            getRawType(returnType) != Call::class.java -> null
            getRawType(callType) != ApiResult::class.java -> null
            else -> ApiResultCallAdapter(resultType, coroutineScope)
        }
    }

    companion object {
        fun create(coroutineScope: CoroutineScope): ApiResultCallAdapterFactory =
            ApiResultCallAdapterFactory(coroutineScope)
    }
}
