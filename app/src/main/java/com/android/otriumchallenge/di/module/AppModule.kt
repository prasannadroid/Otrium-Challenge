package com.android.otriumchallenge.di.module

import android.content.Context
import dagger.Module
import dagger.Provides

/**
 * App module will profile application context.
 *
 * @property applicationContext application context from App class.
 * @constructor Create empty App module
 */
@Module
class AppModule(private val applicationContext: Context) {

    @Provides
    fun provideAppContext() = applicationContext
}