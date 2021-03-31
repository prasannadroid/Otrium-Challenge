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

/**
 * Base activity will be the super class for all activities in this project.
 *
 *
 * @constructor Create empty Base activity
 */
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

    /**
     * On create will be the initial method call for activity and this will
     * initialize butter knife, progressBar, setupView, and Inject dagger objects to
     * the base activity.
     *
     * @param savedInstanceState
     */
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

    /**
     * Setup dependencies will setup dagger object
     * to the AppUtil class.
     *
     */
    private fun setupDependencies() {
        appUtil.context = appContext
        appUtil.endPoint = endPoint
        appUtil.storageManager = storageManager
    }

    /**
     * Setup progress bar will initialize Alert dialog.
     *
     */
    private fun setupProgressBar() {
        progressBar = AlertDialog.Builder(this, R.style.CustomDialog).create()
        progressBar?.setView(LayoutInflater.from(this).inflate(R.layout.progress_bar, null))
        progressBar?.setCancelable(false)

    }

    /**
     * Show progress dialog will display Alert dialog.
     *
     */
    fun showProgressDialog() {
        progressBar?.show()
    }

    /**
     * Show progress dialog will hide Alert dialog.
     *
     */
    fun hideProgressDialog() {
        progressBar?.dismiss()
    }

    /**
     * Get layout res i d will return layout from the sub class.
     *
     * @return Int (Layout)
     */
    abstract fun getLayoutResID(): Int

    /**
     * Setup view will invoke after base activity onCreate.
     *
     * @param savedInstanceState
     */
    abstract fun setupView(savedInstanceState: Bundle?)

    /**
     * Handle api error
     *
     * @param view will be the ui view engaging with the Presenter.
     * @param it will be the Throwable of RX call.
     */
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

    /**
     * Show unknown error when there is no Status code specific error.
     *
     */
    private fun showUnknownError() {
        Toast.makeText(
            appContext.applicationContext,
            appContext.getString(R.string.unknown_error),
            Toast.LENGTH_SHORT
        ).show()

    }

    /**
     * Show network error when there is no network connection.
     *
     */
    private fun showNetworkError() {
        Toast.makeText(
            appContext,
            appContext.getString(R.string.network_error),
            Toast.LENGTH_SHORT
        ).show()

    }

    /**
     * Handle http exception HTTP code 401 error.
     *
     * @param it
     */
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