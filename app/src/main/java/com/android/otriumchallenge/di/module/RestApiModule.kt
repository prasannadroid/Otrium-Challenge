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

@Module
class RestApiModule {

    @Singleton
    @Provides
    fun provideGson(): Gson = GsonBuilder().setLenient().create()

    @Singleton
    @Provides
    fun provideHttpLoginInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE // No logging messages
    }

    @Singleton
    @Provides
    fun provideHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor) =
        OkHttpClient.Builder().connectTimeout(
            Constant.TWENTY_SECONDS, TimeUnit.SECONDS
        ).writeTimeout(Constant.TWENTY_SECONDS, TimeUnit.SECONDS)
            .readTimeout(Constant.THIRTY_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(HttpHeaderInterceptor())
            .addInterceptor(httpLoggingInterceptor).build()

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