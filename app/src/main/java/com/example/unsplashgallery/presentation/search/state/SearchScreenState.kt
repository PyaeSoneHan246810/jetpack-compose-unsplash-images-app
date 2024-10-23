package com.example.unsplashgallery.presentation.search.state

import androidx.paging.PagingData
import com.example.unsplashgallery.domain.model.UnsplashImage
import kotlinx.coroutines.flow.Flow

data class SearchScreenState(
    var searchQuery: String = "",
    var searchResults: Flow<PagingData<UnsplashImage>>? = null
)