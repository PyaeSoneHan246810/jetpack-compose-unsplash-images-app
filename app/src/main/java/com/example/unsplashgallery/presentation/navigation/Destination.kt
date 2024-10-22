package com.example.unsplashgallery.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Destination {
    @Serializable
    data object Home: Destination()
    @Serializable
    data object Search: Destination()
    @Serializable
    data object Favorites: Destination()
    @Serializable
    data class FullImageDisplay(
        val imageId: String
    ): Destination()
    @Serializable
    data class SetWallpaper(
        val imageUrl: String
    ): Destination()
    @Serializable
    data class PhotographerProfile(
        val profileLink: String
    ): Destination()
}