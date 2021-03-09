package com.android.otriumchallenge.di.module

import com.android.otriumchallenge.api.retrofit.RestApi
import dagger.Module
import dagger.Provides

@Module
class RestApiModule {

    @Provides
    fun provideRestApi() = RestApi()
}