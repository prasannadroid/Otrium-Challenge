package com.android.otriumchallenge.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.android.otriumchallenge.App
import com.android.otriumchallenge.R
import com.android.otriumchallenge.api.retrofit.EndpointService
import com.android.otriumchallenge.storage.StorageManager
import com.android.otriumchallenge.util.AppUtil
import com.android.otriumchallenge.view.BaseView
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject

abstract class BaseActivity : AppCompatActivity() {

    private var progressBar: AlertDialog? = null

    @Inject
    lateinit var appUtil: AppUtil

    @Inject
    lateinit var storageManager: StorageManager

    @Inject
    lateinit var endPoint: EndpointService

    @Inject
    lateinit var appContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResID())

        // Initialize butter knife
        ButterKnife.bind(this)

        // Inject AppUtil to the BaseActivity
        App.component?.inject(this)

        // setup progress bar
        setupProgressBar()

        // setup view for child class
        setupView(savedInstanceState)

        setupDependencies()
    }

    private fun setupDependencies() {
        appUtil.context = appContext
        appUtil.endPoint = endPoint
        appUtil.storageManager = storageManager
    }

    private fun setupProgressBar() {
        progressBar = AlertDialog.Builder(this, R.style.CustomDialog).create()
        progressBar?.setView(LayoutInflater.from(this).inflate(R.layout.progress_bar, null))
        progressBar?.setCancelable(false)

    }

    fun showProgressDialog() {
        progressBar?.show()
    }

    fun hideProgressDialog() {
        progressBar?.dismiss()
    }

    abstract fun getLayoutResID(): Int

    abstract fun setupView(savedInstanceState: Bundle?)

    fun handleApiError(view: BaseView, it: Throwable) {
        view.hideProgress()
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
            appContext.applicationContext,
            appContext.getString(R.string.unknown_error),
            Toast.LENGTH_SHORT
        ).show()

    }

    // show Network error message as Toast
    private fun showNetworkError() {
        Toast.makeText(
            appContext,
            appContext.getString(R.string.network_error),
            Toast.LENGTH_SHORT
        ).show()

    }

    // show Authentication error message as Toast
    open fun handleHttpException(it: HttpException) {
        if (it.code() == 401) {
            Toast.makeText(
                appContext.applicationContext,
                appContext.getString(R.string.authentication_error),
                Toast.LENGTH_SHORT
            ).show()
            return
        }
    }
}