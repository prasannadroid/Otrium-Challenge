package com.android.otriumchallenge.di

import com.android.otriumchallenge.di.module.AppModule
import com.android.otriumchallenge.di.module.DataStoreModule
import com.android.otriumchallenge.di.module.RestApiModule
import com.android.otriumchallenge.presenter.BasePresenter
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DataStoreModule::class, RestApiModule::class,AppModule::class]
)
interface AppComponent {
    fun inject(basePresenter: BasePresenter)

}
