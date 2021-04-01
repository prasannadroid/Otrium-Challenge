package com.android.otriumchallenge.network

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.android.otriumchallenge.interfaces.OnNetworkAvailableListener

/**
 * Connection state monitor will handle the network state of the device and pass messages
 *
 * @property onNetworkAvailableListener update UI according to the network update
 * @constructor Create empty Connection state monitor
 */
open class ConnectionStateMonitor(private val onNetworkAvailableListener: OnNetworkAvailableListener) : ConnectivityManager.NetworkCallback() {

    private var networkRequest: NetworkRequest? = null
    private var connectivityManager: ConnectivityManager? = null

    init {
        networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()
    }

    fun enable(context: Context) {
        connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityManager?.registerNetworkCallback(networkRequest!!, this)
    }

    fun unregister(){
        connectivityManager?.unregisterNetworkCallback(this)

    }

    // Likewise, you can have a disable method that simply calls ConnectivityManager.unregisterNetworkCallback(NetworkCallback) too.
    override fun onAvailable(network: Network) {
        onNetworkAvailableListener.onNetworkAvailable(network)
    }

    override fun onUnavailable() {
    }

    override fun onLost(network: Network) {
        onNetworkAvailableListener.onNetworkDisable(network)

    }
}