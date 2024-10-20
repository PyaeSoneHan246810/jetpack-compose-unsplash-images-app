package com.example.unsplashgallery.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileImageDto(
    val small: String,
    val medium: String,
    val large: String,
)