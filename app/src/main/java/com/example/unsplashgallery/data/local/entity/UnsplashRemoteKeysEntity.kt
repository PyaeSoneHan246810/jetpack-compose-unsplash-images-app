package com.example.unsplashgallery.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.unsplashgallery.utils.Constants

@Entity(tableName = Constants.UNSPLASH_REMOTE_KEYS_TABLE)
data class UnsplashRemoteKeysEntity(
    @PrimaryKey
    val id: String,
    val prevPage: Int?,
    val nextPage: Int?
)
