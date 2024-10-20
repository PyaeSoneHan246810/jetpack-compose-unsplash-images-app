package com.example.unsplashgallery.presentation.full_image_display.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FullImageDisplayScreen(
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {

    }
}