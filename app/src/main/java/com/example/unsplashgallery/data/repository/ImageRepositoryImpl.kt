package com.example.unsplashgallery.data.repository

import com.example.unsplashgallery.data.mapper.toDomainModel
import com.example.unsplashgallery.data.mapper.toDomainModelList
import com.example.unsplashgallery.data.remote.api.UnsplashApiService
import com.example.unsplashgallery.domain.model.UnsplashImage
import com.example.unsplashgallery.domain.repository.ImageRepository

class ImageRepositoryImpl(
    private val unsplashApiService: UnsplashApiService
): ImageRepository {
    override suspend fun getEditorialFeedImages(): List<UnsplashImage> = unsplashApiService.getEditorialFeedImages().toDomainModelList()

    override suspend fun getSingleImage(imageId: String): UnsplashImage = unsplashApiService.getSingleImage(imageId).toDomainModel()

}