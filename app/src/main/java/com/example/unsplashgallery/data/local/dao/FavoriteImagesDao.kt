package com.example.unsplashgallery.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.unsplashgallery.data.local.entity.FavoriteImageEntity
import com.example.unsplashgallery.utils.Constants.FAVORITE_IMAGE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteImagesDao {
    @Upsert
    suspend fun upsertFavoriteImage(image: FavoriteImageEntity)

    @Delete
    suspend fun deleteFavoriteImage(image: FavoriteImageEntity)

    @Query("SELECT * FROM $FAVORITE_IMAGE_TABLE")
    fun getAllFavoriteImages(): PagingSource<Int, FavoriteImageEntity>

    @Query("SELECT id FROM $FAVORITE_IMAGE_TABLE")
    fun getAllFavoriteImagesIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM $FAVORITE_IMAGE_TABLE WHERE id = :imageId)")
    suspend fun isImageFavorite(imageId: String): Boolean
}