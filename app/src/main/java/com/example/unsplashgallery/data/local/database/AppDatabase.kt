package com.example.unsplashgallery.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.unsplashgallery.data.local.dao.FavoriteImagesDao
import com.example.unsplashgallery.data.local.entity.FavoriteImageEntity

@Database(
    entities = [FavoriteImageEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract val favoriteImagesDao: FavoriteImagesDao
}