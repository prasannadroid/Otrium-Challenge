package com.android.otriumchallenge.ui

import android.content.Intent
import android.os.Bundle
import com.android.otriumchallenge.R
import com.android.otriumchallenge.presenter.SplashPresenter
import com.android.otriumchallenge.view.SplashView

/**
 * Fist loading screen and navigate to another after 3 seconds.
 *
 * @constructor Create empty Splash activity.
 */
class SplashActivity : BaseActivity(), SplashView {

    /**
     * set layout to the base activity content view.
     */
    override fun getLayoutResID() = R.layout.activity_splash

    /**
     * Splash presenter for Splash activity.
     */
    private var splashPresenter = SplashPresenter(this)

    /**
     * Setup view will call at the first time of the activity.
     *
     * @param savedInstanceState This is bundle from onCreate in BaseActivity class.
     */
    override fun setupView(savedInstanceState: Bundle?) = splashPresenter.navigateProfile()

    /**
     * On countdown complete will call after splash screen loaded and navigate to profile activity.
     *
     */
    override fun onCountdownComplete() = startActivity(Intent(this, ProfileActivity::class.java))

    /**
     * Show progress method will call from view when
     * progress show up.
     *
     */
    override fun showProgress() {
    }

    override fun hideProgress() {
    }
}