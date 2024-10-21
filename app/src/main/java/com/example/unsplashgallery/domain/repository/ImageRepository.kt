package com.example.unsplashgallery.domain.repository

import com.example.unsplashgallery.domain.model.UnsplashImage

interface ImageRepository {
    suspend fun getEditorialFeedImages(): List<UnsplashImage>
    suspend fun getSingleImage(imageId: String): UnsplashImage
}