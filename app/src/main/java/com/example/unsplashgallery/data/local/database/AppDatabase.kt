package com.example.unsplashgallery.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.unsplashgallery.data.local.dao.CachedImagesDao
import com.example.unsplashgallery.data.local.dao.FavoriteImagesDao
import com.example.unsplashgallery.data.local.dao.UnsplashRemoteKeysDao
import com.example.unsplashgallery.data.local.entity.CachedImageEntity
import com.example.unsplashgallery.data.local.entity.FavoriteImageEntity
import com.example.unsplashgallery.data.local.entity.UnsplashRemoteKeysEntity

@Database(
    entities = [CachedImageEntity::class, UnsplashRemoteKeysEntity::class, FavoriteImageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract val cachedImagesDao: CachedImagesDao
    abstract val unsplashRemoteKeysDao: UnsplashRemoteKeysDao
    abstract val favoriteImagesDao: FavoriteImagesDao
}