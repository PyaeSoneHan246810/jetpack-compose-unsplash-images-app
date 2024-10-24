package com.example.unsplashgallery.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.unsplashgallery.data.local.database.AppDatabase
import com.example.unsplashgallery.data.mapper.toDomainModel
import com.example.unsplashgallery.data.mapper.toFavoriteImageEntity
import com.example.unsplashgallery.data.paging.EditorialFeedRemoteMediator
import com.example.unsplashgallery.data.paging.SearchImagesPagingSource
import com.example.unsplashgallery.data.remote.api.UnsplashApiService
import com.example.unsplashgallery.domain.model.UnsplashImage
import com.example.unsplashgallery.domain.repository.ImageRepository
import com.example.unsplashgallery.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ImageRepositoryImpl(
    private val unsplashApiService: UnsplashApiService,
    private val appDatabase: AppDatabase,
): ImageRepository {

    private val cachedImagesDao = appDatabase.cachedImagesDao

    private val favoriteImagesDao = appDatabase.favoriteImagesDao

    @OptIn(ExperimentalPagingApi::class)
    override fun getEditorialFeedImages(): Flow<PagingData<UnsplashImage>> {
        val pager = Pager(
            config = PagingConfig(
                pageSize = Constants.IMAGES_PER_PAGE
            ),
            remoteMediator = EditorialFeedRemoteMediator(
                apiService = unsplashApiService,
                appDatabase = appDatabase,
            ),
            pagingSourceFactory = {
                cachedImagesDao.getAllCachedImages()
            },
        )
        return pager.flow.map { pagingData ->
            pagingData.map { entity ->
                entity.toDomainModel()
            }
        }
    }

    override suspend fun getSingleImage(imageId: String): UnsplashImage = unsplashApiService.getSingleImage(imageId).toDomainModel()

    override fun getSearchImages(query: String): Flow<PagingData<UnsplashImage>> {
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

    override suspend fun toggleFavoriteStatus(image: UnsplashImage) {
        val isFavorite = favoriteImagesDao.isImageFavorite(imageId = image.id)
        if (isFavorite) {
            favoriteImagesDao.deleteFavoriteImage(
                image = image.toFavoriteImageEntity()
            )
        } else {
            favoriteImagesDao.upsertFavoriteImage(
                image = image.toFavoriteImageEntity()
            )
        }
    }

    override fun getFavoriteImagesIds(): Flow<List<String>> = favoriteImagesDao.getAllFavoriteImagesIds()

    override fun getFavoriteImages(): Flow<PagingData<UnsplashImage>> {
        val pager = Pager(
            config = PagingConfig(
                pageSize = Constants.IMAGES_PER_PAGE,
            ),
            pagingSourceFactory = {
                favoriteImagesDao.getAllFavoriteImages()
            }
        )
        return pager.flow.map { pagingData ->
            pagingData.map { entity ->
                entity.toDomainModel()
            }
        }
    }
}