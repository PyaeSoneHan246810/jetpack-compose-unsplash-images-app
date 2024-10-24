package com.example.unsplashgallery.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.unsplashgallery.data.local.entity.UnsplashRemoteKeysEntity
import com.example.unsplashgallery.utils.Constants.UNSPLASH_REMOTE_KEYS_TABLE

@Dao
interface UnsplashRemoteKeysDao {
    @Upsert
    suspend fun upsertRemoteKeysEntities(entities: List<UnsplashRemoteKeysEntity>)

    @Query("DELETE FROM $UNSPLASH_REMOTE_KEYS_TABLE")
    suspend fun deleteAllRemoteKeysEntities()

    @Query("SELECT * FROM $UNSPLASH_REMOTE_KEYS_TABLE WHERE id = :id")
    suspend fun getRemoteKeysEntity(id: String): UnsplashRemoteKeysEntity
}