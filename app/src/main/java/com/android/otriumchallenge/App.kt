package com.android.otriumchallenge

import android.app.Application
import com.android.otriumchallenge.di.AppComponent
import com.android.otriumchallenge.di.DaggerAppComponent
import com.android.otriumchallenge.di.module.AppModule
import com.android.otriumchallenge.di.module.DataStoreModule
import com.android.otriumchallenge.di.module.RestApiModule

open class App : Application() {

    // appComponent will initialize when use
    val appComponent:AppComponent  by lazy(LazyThreadSafetyMode.NONE) {
         DaggerAppComponent.builder()
            .restApiModule(RestApiModule()).appModule(
                AppModule(this)
            )
            .dataStoreModule(DataStoreModule(this)).build()
    }

    companion object {
        private var app: App? = null
        val component by lazy(LazyThreadSafetyMode.NONE) {
            app?.appComponent
        }
    }
    override fun onCreate() {
        super.onCreate()
        app = this
    }

}