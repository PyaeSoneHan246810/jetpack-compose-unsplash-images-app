package com.example.unsplashgallery.presentation.set_wallpaper.view_model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplashgallery.domain.repository.ImageWallpaperManager
import com.example.unsplashgallery.presentation.common.utils.SnackBarEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class SetWallpaperViewModel @Inject constructor(
    private val imageWallpaperManager: ImageWallpaperManager
): ViewModel() {
    var isLoading by mutableStateOf(false)
        private set

    private val _snackBarEvent: Channel<SnackBarEvent> = Channel()
    val snackBarEvent: Flow<SnackBarEvent> = _snackBarEvent.receiveAsFlow()

    fun setImageWallpaper(imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading = true
            _snackBarEvent.send(
                SnackBarEvent(
                    message = "Applying the wallpaper."
                )
            )
            try {
                imageWallpaperManager.setImageWallpaper(imageUrl)
                isLoading = false
                _snackBarEvent.send(
                    SnackBarEvent(
                        message = "Wallpaper is applied."
                    )
                )
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                isLoading = false
                _snackBarEvent.send(
                    SnackBarEvent(
                        message = "Please check your internet connection."
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                isLoading = false
                _snackBarEvent.send(
                    SnackBarEvent(
                        message = "Unable to apply the wallpaper."
                    )
                )
            }
        }
    }
}