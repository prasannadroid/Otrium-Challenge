package com.android.otriumchallenge.ui

import android.content.Intent
import android.os.Bundle
import com.android.otriumchallenge.R
import com.android.otriumchallenge.presenter.SplashPresenter
import com.android.otriumchallenge.view.SplashView

/**
 * Splash activity
 *
 * @constructor Create empty Splash activity
 */
class SplashActivity : BaseActivity(), SplashView {

    override fun getLayoutResID() = R.layout.activity_splash

    private var splashPresenter = SplashPresenter(this)

    override fun setupView(savedInstanceState: Bundle?) = splashPresenter.navigateProfile()

    override fun onCountdownComplete() = startActivity(Intent(this, ProfileActivity::class.java))


    override fun showProgress() {
    }

    override fun hideProgress() {
    }
}