package com.example.unsplashgallery.presentation.set_wallpaper.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplashgallery.domain.repository.ImageWallpaperManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetWallpaperViewModel @Inject constructor(
    private val imageWallpaperManager: ImageWallpaperManager
): ViewModel() {
    fun setImageWallpaper(imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                imageWallpaperManager.setImageWallpaper(imageUrl)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}