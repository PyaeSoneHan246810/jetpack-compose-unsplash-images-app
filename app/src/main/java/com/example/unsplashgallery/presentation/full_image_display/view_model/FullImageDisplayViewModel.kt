package com.example.unsplashgallery.presentation.full_image_display.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.unsplashgallery.domain.model.UnsplashImage
import com.example.unsplashgallery.domain.repository.ImageRepository
import com.example.unsplashgallery.presentation.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullImageDisplayViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val destination = savedStateHandle.toRoute<Destination.FullImageDisplay>()

    private val imageId = destination.imageId

    private val _unsplashImage: MutableStateFlow<UnsplashImage?> = MutableStateFlow(null)
    val unsplashImage: StateFlow<UnsplashImage?> = _unsplashImage.asStateFlow()

    init {
        getUnsplashImage()
    }

    private fun getUnsplashImage() {
        viewModelScope.launch {
            try {
                val unsplashImageResult = imageRepository.getSingleImage(
                    imageId = imageId
                )
                _unsplashImage.value = unsplashImageResult
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}