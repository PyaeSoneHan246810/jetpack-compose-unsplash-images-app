package com.example.unsplashgallery.presentation.home.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplashgallery.domain.model.UnsplashImage
import com.example.unsplashgallery.domain.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val imageRepository: ImageRepository
): ViewModel() {
    private val _unsplashImages: MutableStateFlow<List<UnsplashImage>> = MutableStateFlow(listOf())
    val unsplashImages: StateFlow<List<UnsplashImage>> = _unsplashImages.asStateFlow()

    init {
        getUnsplashImages()
    }

    private fun getUnsplashImages() {
        viewModelScope.launch {
            try {
                val unsplashImagesResult = imageRepository.getEditorialFeedImages()
                _unsplashImages.value = unsplashImagesResult
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}