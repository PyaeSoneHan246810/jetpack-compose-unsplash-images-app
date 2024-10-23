package com.example.unsplashgallery.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.unsplashgallery.data.mapper.toDomainModel
import com.example.unsplashgallery.data.mapper.toDomainModelList
import com.example.unsplashgallery.data.paging.SearchImagesPagingSource
import com.example.unsplashgallery.data.remote.api.UnsplashApiService
import com.example.unsplashgallery.domain.model.UnsplashImage
import com.example.unsplashgallery.domain.repository.ImageRepository
import com.example.unsplashgallery.utils.Constants
import kotlinx.coroutines.flow.Flow

class ImageRepositoryImpl(
    private val unsplashApiService: UnsplashApiService
): ImageRepository {
    override suspend fun getEditorialFeedImages(): List<UnsplashImage> = unsplashApiService.getEditorialFeedImages().toDomainModelList()

    override suspend fun getSingleImage(imageId: String): UnsplashImage = unsplashApiService.getSingleImage(imageId).toDomainModel()

    override suspend fun getSearchImages(query: String): Flow<PagingData<UnsplashImage>> {
        val pager = Pager(
            config = PagingConfig(
                pageSize = Constants.IMAGES_PER_PAGE,

            ),
            pagingSourceFactory = {
                SearchImagesPagingSource(
                    searchQuery = query,
                    unsplashApiService = unsplashApiService
                )
            },
        )
        return pager.flow
    }
}