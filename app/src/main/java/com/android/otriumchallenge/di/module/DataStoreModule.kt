package com.android.otriumchallenge.di.module

import android.content.Context
import com.android.otriumchallenge.storage.StorageManager
import com.android.otriumchallenge.util.Constant
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataStoreModule(applicationContext: Context) {

    private var sharedPreferences = applicationContext.getSharedPreferences(
        Constant.SHARED_PREF, Context.MODE_PRIVATE
    )

    @Singleton
    @Provides
    fun provideDataStore() = StorageManager(sharedPreferences)

}