package com.drip.dripapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber

class NetworkStateUtil(context: Context, snackbar: Snackbar?) {
    private val connectivityManager = getSystemService(context, ConnectivityManager::class.java) as ConnectivityManager

    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onLost(network: Network) {
            super.onLost(network)
            snackbar?.show()
        }
    }

    fun registerNetworkCallback(){
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun unRegisterNetworkCallback(){
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}