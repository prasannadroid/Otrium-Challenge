package com.android.otriumchallenge.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build


class NetworkServiceManager {

    companion object {
        fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            // True condition will work above android Marshmello
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true // when wifi enabled
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true // when mobile data enabled
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true // when Ethernet enabled
                    else -> false
                }
            } else {
                // for below android Marshmello versions
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true // when wifi enabled
                            ConnectivityManager.TYPE_MOBILE -> true // when mobile data enabled
                            ConnectivityManager.TYPE_ETHERNET -> true // when Ethernet enabled
                            else -> false
                        }

                    }
                }
            }

            return result
        }
    }
}