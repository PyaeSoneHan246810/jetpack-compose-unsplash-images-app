package com.example.unsplashgallery.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.example.unsplashgallery.domain.repository.NetworkConnectivityManager
import com.example.unsplashgallery.utils.NetworkStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn

class NetworkConnectivityManagerImpl(
    context: Context,
    scope: CoroutineScope
): NetworkConnectivityManager {
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val networkStatus: StateFlow<NetworkStatus> = observeNetworkStatusFlow().stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = 5000
        ),
        initialValue = NetworkStatus.Disconnected
    )

    private fun observeNetworkStatusFlow(): Flow<NetworkStatus> {
        return callbackFlow {
            val request = NetworkRequest
                .Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build()
            val networkCallback = object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    trySend(NetworkStatus.Connected)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    trySend(NetworkStatus.Disconnected)
                }
            }
            connectivityManager.registerNetworkCallback(
                request,
                networkCallback
            )
            awaitClose {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        }.distinctUntilChanged()
    }
}