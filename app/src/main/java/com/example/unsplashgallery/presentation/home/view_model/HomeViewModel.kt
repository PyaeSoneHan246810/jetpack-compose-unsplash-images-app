package com.example.unsplashgallery.presentation.home.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.unsplashgallery.domain.model.UnsplashImage
import com.example.unsplashgallery.domain.repository.ImageRepository
import com.example.unsplashgallery.presentation.common.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val imageRepository: ImageRepository
): ViewModel() {
    private val _unsplashImages: MutableStateFlow<Flow<PagingData<UnsplashImage>>?> = MutableStateFlow(null)
    val unsplashImages: StateFlow<Flow<PagingData<UnsplashImage>>?> = _unsplashImages.asStateFlow()

    private val _favoriteImagesIds: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val favoriteImagesIds: StateFlow<List<String>> = _favoriteImagesIds.asStateFlow()

    private val _snackBarEvent: Channel<SnackBarEvent> = Channel()
    val snackBarEvent: Flow<SnackBarEvent> = _snackBarEvent.receiveAsFlow()

    init {
        getUnsplashImages()
        getFavoriteImagesIds()
    }

    private fun getUnsplashImages() {
        try {
            _unsplashImages.value = imageRepository.getEditorialFeedImages()
                .cachedIn(viewModelScope)
        } catch (e: UnknownHostException) {
            e.printStackTrace()
            viewModelScope.launch {
                _snackBarEvent.send(
                    SnackBarEvent(
                        message = "Please check your internet connection."
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            viewModelScope.launch {
                _snackBarEvent.send(
                    SnackBarEvent(
                        message = "Unable to load images."
                    )
                )
            }
        }
    }

    private fun getFavoriteImagesIds() {
        imageRepository.getFavoriteImagesIds().onEach { favoriteImagesIds ->
            _favoriteImagesIds.value = favoriteImagesIds
        }.catch {
            _snackBarEvent.send(
                SnackBarEvent(
                    message = "Something went wrong."
                )
            )
        }.launchIn(viewModelScope)
    }

    fun toggleFavoriteStatus(image: UnsplashImage) {
        viewModelScope.launch {
            try {
                imageRepository.toggleFavoriteStatus(
                    image = image
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _snackBarEvent.send(
                    SnackBarEvent(
                        message = "Unable to add to favorites."
                    )
                )
            }
        }
    }
}