package com.android.otriumchallenge.di.module

import com.android.otriumchallenge.api.retrofit.EndpointService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class EndpointModule {

    @Provides
    fun provideEndpont(retrofit: Retrofit) = retrofit?.create(EndpointService::class.java)
}