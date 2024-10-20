package com.example.unsplashgallery.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnsplashImageDto(
    val id: String,
    val description: String?,
    val width: Int,
    val height: Int,
    @SerialName("urls")
    val urlsDto: UrlsDto,
    @SerialName("user")
    val userDto: UserDto,
)