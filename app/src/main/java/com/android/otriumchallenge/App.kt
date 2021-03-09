package com.android.otriumchallenge

import android.app.Application
import com.android.otriumchallenge.di.AppComponent
import com.android.otriumchallenge.di.DaggerAppComponent
import com.android.otriumchallenge.di.module.AppModule
import com.android.otriumchallenge.di.module.DataStoreModule
import com.android.otriumchallenge.di.module.RestApiModule

open class App : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        iniDagger()
    }

    private fun iniDagger() {
        appComponent = DaggerAppComponent.builder()
            .restApiModule(RestApiModule()).appModule(
                AppModule(this)
            )
            .dataStoreModule(DataStoreModule(this)).build()

    }
}