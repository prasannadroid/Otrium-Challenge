package com.android.otriumchallenge.di.module

import com.android.otriumchallenge.api.retrofit.EndpointService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
class EndpointModule {

    @Singleton
    @Provides
    fun provideEndpoint(retrofit: Retrofit): EndpointService = retrofit.create(EndpointService::class.java)
}