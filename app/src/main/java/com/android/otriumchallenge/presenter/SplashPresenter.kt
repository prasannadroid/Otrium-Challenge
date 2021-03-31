package com.android.otriumchallenge.presenter

import android.os.Handler
import com.android.otriumchallenge.view.SplashView

class SplashPresenter(private val splashView: SplashView) {

    var duration = 2000L

    fun navigateProfile() {
        Handler().postDelayed({
            splashView.onCountdownComplete()
        }, duration)
    }
}