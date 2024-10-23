package com.example.unsplashgallery.domain.repository

import androidx.paging.PagingData
import com.example.unsplashgallery.domain.model.UnsplashImage
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun getEditorialFeedImages(): List<UnsplashImage>
    suspend fun getSingleImage(imageId: String): UnsplashImage
    suspend fun getSearchImages(query: String): Flow<PagingData<UnsplashImage>>
}