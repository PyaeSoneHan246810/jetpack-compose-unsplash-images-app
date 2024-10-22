package com.example.unsplashgallery.presentation.set_wallpaper.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.unsplashgallery.R

@Composable
fun SetWallpaperScreen(
    modifier: Modifier = Modifier,
    imageUrl: String,
    onConfirmClick: (imageUrl: String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = rememberAsyncImagePainter(
                    model = imageUrl
                ),
                contentDescription = stringResource(R.string.wallpaper_image),
                contentScale = ContentScale.Crop
            )
            IconButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(
                        bottom = 12.dp
                    )
                    .navigationBarsPadding(),
                onClick = {
                    onConfirmClick(imageUrl)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = stringResource(R.string.confirm)
                )
            }
        }
    }
}