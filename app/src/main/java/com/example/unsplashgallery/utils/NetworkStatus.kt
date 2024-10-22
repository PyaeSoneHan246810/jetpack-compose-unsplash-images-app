package com.example.unsplashgallery.utils

sealed class NetworkStatus {
    data object Connected: NetworkStatus()
    data object Disconnected: NetworkStatus()
}