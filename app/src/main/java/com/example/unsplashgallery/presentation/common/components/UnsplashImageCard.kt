package com.example.unsplashgallery.presentation.common.components

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.unsplashgallery.domain.model.UnsplashImage

@Composable
fun UnsplashImageCard(
    modifier: Modifier = Modifier,
    unsplashImage: UnsplashImage,
    onClick: (imageId: String) -> Unit,
    onImageCardDragStart: (previewImage: UnsplashImage) -> Unit,
    onImageCardDragEnd: () -> Unit,
    onImageCardDragCancel: () -> Unit,
) {
    val context = LocalContext.current
    val imageCardAspectRadio by remember {
        derivedStateOf {
            unsplashImage.width.toFloat() / unsplashImage.height.toFloat()
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(imageCardAspectRadio)
            .then(modifier)
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress(
                    onDragStart = {
                        onImageCardDragStart(unsplashImage)
                    },
                    onDragCancel = onImageCardDragCancel,
                    onDragEnd = onImageCardDragEnd,
                    onDrag = { _, _ -> }
                )
            },
        shape = RoundedCornerShape(12.dp),
        onClick = {
            onClick(unsplashImage.id)
        },
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxSize(),
            model = ImageRequest
                .Builder(context)
                .data(unsplashImage.imageUrlSmall)
                .crossfade(true)
                .build(),
            contentDescription = unsplashImage.description,
            contentScale = ContentScale.FillBounds,
        )
    }
}