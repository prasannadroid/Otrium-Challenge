package com.android.otriumchallenge.di

import com.android.otriumchallenge.di.module.AppModule
import com.android.otriumchallenge.di.module.DataStoreModule
import com.android.otriumchallenge.di.module.EndpointModule
import com.android.otriumchallenge.di.module.RestApiModule
import com.android.otriumchallenge.ui.BaseActivity
import com.android.otriumchallenge.util.AppUtil
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DataStoreModule::class, RestApiModule::class,AppModule::class, EndpointModule::class]
)
interface AppComponent {
    fun inject(baseActivity: BaseActivity)
    fun inject(appUtil: AppUtil)

}
