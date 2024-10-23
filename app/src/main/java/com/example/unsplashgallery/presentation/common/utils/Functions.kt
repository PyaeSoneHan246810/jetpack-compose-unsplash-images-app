package com.example.unsplashgallery.presentation.common.utils

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun rememberWindowInsetsControllerCompat(): WindowInsetsControllerCompat {
    val window = (LocalContext.current as Activity).window
    return remember {
        WindowCompat.getInsetsController(window, window.decorView)
    }
}

fun WindowInsetsControllerCompat.toggleSystemBarsVisibility(visible: Boolean) {
    if (visible) {
        show(WindowInsetsCompat.Type.systemBars())
    } else {
        hide(WindowInsetsCompat.Type.systemBars())
    }
}