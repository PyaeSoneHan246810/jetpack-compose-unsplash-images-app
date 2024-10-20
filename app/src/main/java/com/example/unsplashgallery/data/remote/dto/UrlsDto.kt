package com.example.unsplashgallery.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UrlsDto(
    val full: String,
    val raw: String,
    val small: String,
    val regular: String,
    val thumb: String
)