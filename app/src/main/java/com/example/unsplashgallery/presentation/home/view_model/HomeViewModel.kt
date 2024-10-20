package com.example.unsplashgallery.presentation.home.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplashgallery.data.mapper.toDomainModel
import com.example.unsplashgallery.di.AppModule
import com.example.unsplashgallery.domain.model.UnsplashImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(): ViewModel() {
    private val _unsplashImages: MutableStateFlow<List<UnsplashImage>> = MutableStateFlow(listOf())
    val unsplashImages: StateFlow<List<UnsplashImage>> = _unsplashImages.asStateFlow()

    init {
        getUnsplashImages()
    }

    private fun getUnsplashImages() {
        viewModelScope.launch {
            val dtoImages = AppModule.provideUnsplashApiService().getEditorialFeedImages()
            val domainModelImages = dtoImages.map { dtoImage ->
                dtoImage.toDomainModel()
            }
            _unsplashImages.value = domainModelImages
        }
    }
}