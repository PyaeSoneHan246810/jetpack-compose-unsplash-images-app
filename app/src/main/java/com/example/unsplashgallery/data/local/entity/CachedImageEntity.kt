package com.example.unsplashgallery.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.unsplashgallery.utils.Constants

@Entity(tableName = Constants.CACHED_IMAGE_TABLE)
data class CachedImageEntity(
    @PrimaryKey
    val id: String,
    val imageUrlSmall: String,
    val imageUrlRegular: String,
    val imageUrlRaw: String,
    val photographerName: String,
    val photographerUsername: String,
    val photographerProfileImageUrl: String,
    val photographerProfileLink: String,
    val width: Int,
    val height: Int,
    val description: String?
)
