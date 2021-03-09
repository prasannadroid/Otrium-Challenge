package com.android.otriumchallenge.presenter

import android.content.Context
import android.widget.Toast
import com.android.otriumchallenge.App
import com.android.otriumchallenge.R
import com.android.otriumchallenge.api.retrofit.RestApi
import com.android.otriumchallenge.storage.StorageManager
import com.android.otriumchallenge.view.BaseView
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

open class BasePresenter(private val baseView: BaseView) {

    @Inject
    lateinit var storageManager: StorageManager

    @Inject
    lateinit var restAPI: RestApi

    @Inject
    lateinit var appContext: Context

    init {
        App.appComponent?.inject(this)
    }

    fun getApi() = restAPI

    fun getStoreManager() = storageManager

    fun getApp() = appContext


    fun handleError(it: Throwable) {
        baseView.hideProgress()
        it.printStackTrace()

        when (it) {
            is UnknownHostException -> {
                showNetworkError()
                return
            }
            is HttpException -> handleHttpException(it)
            else -> showUnknownError()
        }
    }

    // show unknown error message as Toast
    private fun showUnknownError() {
        Toast.makeText(
            getApp().applicationContext,
            getApp().getString(R.string.unknown_error),
            Toast.LENGTH_SHORT
        ).show()

    }

    // show Network error message as Toast
    private fun showNetworkError() {
        Toast.makeText(
            getApp(),
            getApp().getString(R.string.network_error),
            Toast.LENGTH_SHORT
        ).show()

    }

    // show Authentication error message as Toast
    open fun handleHttpException(it: HttpException) {
        if (it.code() == 401) {
            Toast.makeText(
                getApp().applicationContext,
                getApp().getString(R.string.authentication_error),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
    }

}