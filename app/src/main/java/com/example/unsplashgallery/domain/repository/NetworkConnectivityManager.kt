package com.example.unsplashgallery.domain.repository

import com.example.unsplashgallery.utils.NetworkStatus
import kotlinx.coroutines.flow.StateFlow

interface NetworkConnectivityManager {
    val networkStatus: StateFlow<NetworkStatus>
}