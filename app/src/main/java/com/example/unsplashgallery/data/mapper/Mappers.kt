package com.example.unsplashgallery.data.mapper

import com.example.unsplashgallery.data.remote.dto.UnsplashImageDto
import com.example.unsplashgallery.domain.model.UnsplashImage

fun UnsplashImageDto.toDomainModel(): UnsplashImage {
    return UnsplashImage(
        id = this.id,
        imageUrlSmall = this.urlsDto.small,
        imageUrlRegular = this.urlsDto.regular,
        imageUrlRaw = this.urlsDto.raw,
        photographerName = this.userDto.name,
        photographerUsername = this.userDto.username,
        photographerProfileImageUrl = this.userDto.profileImageDto.medium,
        photographerProfileLink = this.userDto.userLinksDto.html,
        width = this.width,
        height = this.height,
        description = this.description
    )
}

fun List<UnsplashImageDto>.toDomainModelList(): List<UnsplashImage> {
    return this.map { dto ->
        dto.toDomainModel()
    }
}