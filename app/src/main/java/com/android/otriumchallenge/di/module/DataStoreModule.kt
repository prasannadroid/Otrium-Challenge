package com.android.otriumchallenge.di.module

import android.content.Context
import android.content.SharedPreferences
import com.android.otriumchallenge.storage.StorageManager
import com.android.otriumchallenge.util.Constant
import dagger.Module
import dagger.Provides

@Module
class DataStoreModule(private val applicationContext: Context) {

    private var sharedPreferences = applicationContext.getSharedPreferences(
        Constant.SHARED_PREF, Context.MODE_PRIVATE
    )

    @Provides
    fun provideDataStore() = StorageManager(sharedPreferences)

}