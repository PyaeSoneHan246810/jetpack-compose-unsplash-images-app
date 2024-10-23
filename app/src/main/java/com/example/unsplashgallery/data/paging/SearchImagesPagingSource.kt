package com.example.unsplashgallery.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplashgallery.data.mapper.toDomainModelList
import com.example.unsplashgallery.data.remote.api.UnsplashApiService
import com.example.unsplashgallery.domain.model.UnsplashImage

class SearchImagesPagingSource(
    private val searchQuery: String,
    private val unsplashApiService: UnsplashApiService
): PagingSource<Int, UnsplashImage>() {
    companion object {
        private const val STARTING_PAGE = 1
    }
    override fun getRefreshKey(state: PagingState<Int, UnsplashImage>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashImage> {
        val currentPage = params.key ?: STARTING_PAGE
        return try {
            val searchResult = unsplashApiService.getSearchImages(
                query = searchQuery,
                page = currentPage,
                perPage = params.loadSize,
            )
            val endOfPaginationReached = searchResult.images.isEmpty()
            LoadResult.Page(
                data = searchResult.images.toDomainModelList(),
                prevKey = if (currentPage == STARTING_PAGE) null else currentPage - 1,
                nextKey = if (endOfPaginationReached) null else currentPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(
                throwable = e
            )
        }
    }
}