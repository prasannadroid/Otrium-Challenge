package com.android.otriumchallenge.api.interceptor

import com.android.otriumchallenge.util.Constant
import okhttp3.Interceptor
import okhttp3.Response

class HttpHeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        return chain.proceed(
            chain.request().newBuilder().header(
                Constant.HEADER_CONTENT_TYPE,
                Constant.HEADER_APPLICATION_JSON
            ).header(Constant.HEADER_AUTHORIZATION, Constant.BEARER_TOKEN).build()
        )
    }
}