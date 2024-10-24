package com.example.unsplashgallery.data.mapper

import com.example.unsplashgallery.data.local.entity.CachedImageEntity
import com.example.unsplashgallery.data.local.entity.FavoriteImageEntity
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

fun UnsplashImageDto.toCachedImageEntity(): CachedImageEntity {
    return CachedImageEntity(
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

fun UnsplashImage.toFavoriteImageEntity(): FavoriteImageEntity {
    return FavoriteImageEntity(
        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRegular = this.imageUrlRegular,
        imageUrlRaw = this.imageUrlRaw,
        photographerName = this.photographerName,
        photographerUsername = this.photographerUsername,
        photographerProfileImageUrl = this.photographerProfileImageUrl,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width,
        height = this.height,
        description = this.description
    )
}

fun FavoriteImageEntity.toDomainModel(): UnsplashImage {
    return UnsplashImage(
        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRegular = this.imageUrlRegular,
        imageUrlRaw = this.imageUrlRaw,
        photographerName = this.photographerName,
        photographerUsername = this.photographerUsername,
        photographerProfileImageUrl = this.photographerProfileImageUrl,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width,
        height = this.height,
        description = this.description
    )
}

fun CachedImageEntity.toDomainModel(): UnsplashImage {
    return UnsplashImage(
        id = this.id,
        imageUrlSmall = this.imageUrlSmall,
        imageUrlRegular = this.imageUrlRegular,
        imageUrlRaw = this.imageUrlRaw,
        photographerName = this.photographerName,
        photographerUsername = this.photographerUsername,
        photographerProfileImageUrl = this.photographerProfileImageUrl,
        photographerProfileLink = this.photographerProfileLink,
        width = this.width,
        height = this.height,
        description = this.description
    )
}