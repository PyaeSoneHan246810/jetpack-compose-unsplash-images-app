package com.example.unsplashgallery.domain.repository

import androidx.paging.PagingData
import com.example.unsplashgallery.domain.model.UnsplashImage
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getEditorialFeedImages(): List<UnsplashImage>
    suspend fun getSingleImage(imageId: String): UnsplashImage
    fun getSearchImages(query: String): Flow<PagingData<UnsplashImage>>
    suspend fun toggleFavoriteStatus(image: UnsplashImage)
    fun getFavoriteImagesIds(): Flow<List<String>>
    fun getFavoriteImages(): Flow<PagingData<UnsplashImage>>
}