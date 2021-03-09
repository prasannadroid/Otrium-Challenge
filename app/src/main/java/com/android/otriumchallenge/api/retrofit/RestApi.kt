package com.android.otriumchallenge.api.retrofit

import com.android.otriumchallenge.api.interceptor.HttpHeaderInterceptor
import com.android.otriumchallenge.api.response.UserResponse
import com.android.otriumchallenge.util.Constant
import com.android.otriumchallenge.util.Constant.Companion.THIRTY_SECONDS
import com.android.otriumchallenge.util.Constant.Companion.TWENTY_SECONDS
import com.google.gson.GsonBuilder
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

open class RestApi {

    private lateinit var retrofit: Retrofit
    private lateinit var restService: RestService

    init {
        initRetrofit()
        initServices()
    }

    private fun initRetrofit() {

        // initialize logging interceptor to view log messages
        //  Testing purposes only
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Gson client
        val gSon = GsonBuilder().setLenient().create()

        // okHttp
        val client = OkHttpClient.Builder().connectTimeout(
            TWENTY_SECONDS, TimeUnit.SECONDS
        ).writeTimeout(TWENTY_SECONDS, TimeUnit.SECONDS)
            .readTimeout(THIRTY_SECONDS, TimeUnit.SECONDS).addInterceptor(HttpHeaderInterceptor())
            .addInterceptor(loggingInterceptor).build()

        // here will initialize retrofit client with three Converter Factories
        retrofit =
            Retrofit.Builder().client(client)
                .addConverterFactory(
                    ScalarsConverterFactory.create()
                ).addConverterFactory(GsonConverterFactory.create(gSon))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).baseUrl(getApiUrl())
                .build()


    }

    private fun initServices() {
        restService = retrofit.create(RestService::class.java)
    }

    // return Graph API base url
    private fun getApiUrl() = Constant.BASE_URL

    // fetch from Graph API
    fun fetchUserFromGit(query: String): Single<UserResponse> {
        return restService.fetchUserFromGit(query).subscribeOn(Schedulers.io())
    }

}