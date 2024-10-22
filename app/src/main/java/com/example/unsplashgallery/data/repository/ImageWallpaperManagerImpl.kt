package com.example.unsplashgallery.data.repository

import android.app.WallpaperManager
import android.content.Context
import android.graphics.BitmapFactory
import com.example.unsplashgallery.domain.repository.ImageWallpaperManager
import java.net.URL

class ImageWallpaperManagerImpl(
    context: Context
): ImageWallpaperManager {
    private val wallpaperManager = WallpaperManager.getInstance(context)
    override fun setImageWallpaper(imageUrl: String) {
        try {
            val bitmap = BitmapFactory.decodeStream(
                URL(imageUrl).openConnection().getInputStream()
            )
            wallpaperManager.setBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}