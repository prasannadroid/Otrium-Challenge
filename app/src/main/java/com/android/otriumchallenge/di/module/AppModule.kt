package com.android.otriumchallenge.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val applicationContext: Context) {

    @Provides
    fun provideAppContext() = applicationContext
}