package com.example.unsplashgallery.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val name: String,
    val username: String,
    @SerialName("profile_image")
    val profileImageDto: ProfileImageDto,
    @SerialName("links")
    val userLinksDto: UserLinksDto,
)