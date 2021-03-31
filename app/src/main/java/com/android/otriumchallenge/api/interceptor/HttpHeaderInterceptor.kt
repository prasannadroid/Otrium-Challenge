package com.android.otriumchallenge.api.interceptor

import com.android.otriumchallenge.util.Constant
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Http header interceptor will handle the ContentType header and the
 * Authorization header with toke.
 *
 * @constructor Create empty Http header interceptor.
 */
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