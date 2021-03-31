package com.android.otriumchallenge.di.module

import com.android.otriumchallenge.api.interceptor.HttpHeaderInterceptor
import com.android.otriumchallenge.util.Constant
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Rest api module will provide Gson, LoginInterceptor, and Retrofit objects.
 *
 * @constructor Create empty Rest api module.
 */
@Module
class RestApiModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    /**
     * Provide http login interceptor will give HttpLoggingInterceptor object with
     * disabled login level.
     *
     */
    @Singleton
    @Provides
    fun provideHttpLoginInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE // No logging messages
    }

    /**
     * Provide http client will give okHTTPClient object.
     *
     * @param httpLoggingInterceptor from dagger
     */
    @Singleton
    @Provides
    fun provideHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder().connectTimeout(
            Constant.TWENTY_SECONDS, TimeUnit.SECONDS
        ).writeTimeout(Constant.TWENTY_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constant.THIRTY_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(HttpHeaderInterceptor())
            .addInterceptor(httpLoggingInterceptor).build()

    /**
     * Provide retrofit object with base url.
     *
     * @param httpClient
     * @param gSon
     * @return
     */
    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient, gSon: Gson): Retrofit =
        Retrofit.Builder().client(httpClient)
            .addConverterFactory(
                ScalarsConverterFactory.create()
            ).addConverterFactory(GsonConverterFactory.create(gSon))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(Constant.BASE_URL) // set the base url here
            .build()


}