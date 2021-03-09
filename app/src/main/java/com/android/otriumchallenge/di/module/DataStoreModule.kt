package com.android.otriumchallenge.di.module

import android.content.Context
import com.android.otriumchallenge.storage.StorageManager
import dagger.Module
import dagger.Provides

@Module
class DataStoreModule(private val applicationContext: Context) {

    @Provides
    fun provideDataStore() = StorageManager(applicationContext)

}