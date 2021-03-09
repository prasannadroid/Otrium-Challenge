package com.android.otriumchallenge.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import butterknife.ButterKnife
import com.android.otriumchallenge.R

abstract class BaseActivity : AppCompatActivity() {

    private var progressBar: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResID())
        ButterKnife.bind(this)
        setupProgressBar()
        setupView(savedInstanceState)
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

}