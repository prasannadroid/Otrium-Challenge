package com.android.otriumchallenge.presenter

import android.os.Handler
import com.android.otriumchallenge.view.SplashView

/**
 * Splash presenter will handle business logic of SplashActivity.
 *
 * @property splashView
 * @constructor Create empty Splash presenter
 */
class SplashPresenter(private val splashView: SplashView) {

    var duration = 2000L

    fun navigateProfile() {
        Handler().postDelayed({
            splashView.onCountdownComplete()
        }, duration)
    }
}