package com.example.unsplashgallery.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.unsplashgallery.data.local.database.AppDatabase
import com.example.unsplashgallery.data.local.entity.CachedImageEntity
import com.example.unsplashgallery.data.local.entity.UnsplashRemoteKeysEntity
import com.example.unsplashgallery.data.mapper.toCachedImageEntity
import com.example.unsplashgallery.data.remote.api.UnsplashApiService

@OptIn(ExperimentalPagingApi::class)
class EditorialFeedRemoteMediator(
    private val apiService: UnsplashApiService,
    private val appDatabase: AppDatabase,
): RemoteMediator<Int, CachedImageEntity>() {

    private val cachedImagesDao = appDatabase.cachedImagesDao

    private val unsplashRemoteKeysDao = appDatabase.unsplashRemoteKeysDao

    companion object {
        private const val STARTING_PAGE = 1
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CachedImageEntity>
    ): MediatorResult {
        try {
            val currentPage = when(loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextPage?.minus(1) ?: STARTING_PAGE
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeysForFirstItem(state)
                    remoteKeys?.prevPage ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeysForLastItem(state)
                    remoteKeys?.nextPage ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }
            val itemsPerPage = state.config.pageSize
            val apiResponse = apiService.getEditorialFeedImages(
                page = currentPage,
                perPage = itemsPerPage
            )
            val endOfPaginationReached = apiResponse.isEmpty()
            val prevPage = if (currentPage == 1) null else currentPage - 1
            val nextPage = if (endOfPaginationReached) null else currentPage + 1
            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    unsplashRemoteKeysDao.deleteAllRemoteKeysEntities()
                    cachedImagesDao.deleteAllCachedImages()
                }
                val remoteKeys = apiResponse.map { unsplashImageDto ->
                    UnsplashRemoteKeysEntity(
                        id = unsplashImageDto.id,
                        prevPage = prevPage,
                        nextPage = nextPage,
                    )
                }
                val cachedImages = apiResponse.map { unsplashImageDto ->
                    unsplashImageDto.toCachedImageEntity()
                }
                unsplashRemoteKeysDao.upsertRemoteKeysEntities(remoteKeys)
                cachedImagesDao.upsertCachedImages(cachedImages)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            return MediatorResult.Error(
                throwable = e
            )
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, CachedImageEntity>): UnsplashRemoteKeysEntity? {
        return state.anchorPosition?.let {  position ->
            state.closestItemToPosition(position)?.id?.let {  id ->
                unsplashRemoteKeysDao.getRemoteKeysEntity(id)
            }
        }
    }

    private suspend fun getRemoteKeysForFirstItem(state: PagingState<Int, CachedImageEntity>): UnsplashRemoteKeysEntity? {
        return state.pages.firstOrNull { page ->
            page.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { cachedImageEntity ->
            unsplashRemoteKeysDao.getRemoteKeysEntity(
                id = cachedImageEntity.id
            )
        }
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, CachedImageEntity>): UnsplashRemoteKeysEntity? {
        return state.pages.lastOrNull { page ->
            page.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { cachedImageEntity ->
            unsplashRemoteKeysDao.getRemoteKeysEntity(
                id = cachedImageEntity.id
            )
        }
    }
}