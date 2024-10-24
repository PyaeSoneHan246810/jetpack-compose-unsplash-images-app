package com.example.unsplashgallery.presentation.full_image_display.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.unsplashgallery.domain.model.UnsplashImage
import com.example.unsplashgallery.domain.repository.ImageDownloadManager
import com.example.unsplashgallery.domain.repository.ImageRepository
import com.example.unsplashgallery.presentation.common.utils.SnackBarEvent
import com.example.unsplashgallery.presentation.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class FullImageDisplayViewModel @Inject constructor(
    private val imageRepository: ImageRepository,
    private val imageDownloadManager: ImageDownloadManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val destination = savedStateHandle.toRoute<Destination.FullImageDisplay>()

    private val imageId = destination.imageId

    private val _unsplashImage: MutableStateFlow<UnsplashImage?> = MutableStateFlow(null)
    val unsplashImage: StateFlow<UnsplashImage?> = _unsplashImage.asStateFlow()

    private val _snackBarEvent: Channel<SnackBarEvent> = Channel()
    val snackBarEvent: Flow<SnackBarEvent> = _snackBarEvent.receiveAsFlow()

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
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                _snackBarEvent.send(
                    SnackBarEvent(
                        message = "Please check your internet connection."
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _snackBarEvent.send(
                    SnackBarEvent(
                        message = "Unable to load the wallpaper."
                    )
                )
            }
        }
    }

    fun downloadImage(url: String, title: String?) {
        viewModelScope.launch {
            _snackBarEvent.send(
                SnackBarEvent(
                    message = "Downloading the wallpaper."
                )
            )
            try {
                imageDownloadManager.downloadImageFile(url, title)
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                _snackBarEvent.send(
                    SnackBarEvent(
                        message = "Please check your internet connection."
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _snackBarEvent.send(
                    SnackBarEvent(
                        message = "Unable to download the wallpaper."
                    )
                )
            }
        }
    }
}