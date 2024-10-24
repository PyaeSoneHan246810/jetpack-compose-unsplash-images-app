package com.example.unsplashgallery.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.unsplashgallery.data.local.entity.CachedImageEntity
import com.example.unsplashgallery.utils.Constants.CACHED_IMAGE_TABLE

@Dao
interface CachedImagesDao {
    @Upsert
    suspend fun upsertCachedImages(images: List<CachedImageEntity>)

    @Query("DELETE FROM $CACHED_IMAGE_TABLE")
    suspend fun deleteAllCachedImages()

    @Query("SELECT * FROM $CACHED_IMAGE_TABLE")
    fun getAllCachedImages(): PagingSource<Int, CachedImageEntity>
}