package com.android.otriumchallenge.interfaces

import android.net.Network

interface OnNetworkAvailableListener {

    fun onNetworkAvailable(network: Network)

    fun onNetworkDisable(network: Network)
}