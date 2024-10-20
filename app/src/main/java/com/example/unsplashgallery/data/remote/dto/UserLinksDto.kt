package com.example.unsplashgallery.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserLinksDto(
    val html: String,
    val likes: String,
    val photos: String,
    val portfolio: String,
    val self: String
)