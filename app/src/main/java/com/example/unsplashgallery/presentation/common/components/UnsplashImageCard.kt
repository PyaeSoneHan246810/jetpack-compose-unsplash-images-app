package com.example.unsplashgallery.presentation.common.components

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.unsplashgallery.R
import com.example.unsplashgallery.domain.model.UnsplashImage

@Composable
fun UnsplashImageCard(
    modifier: Modifier = Modifier,
    unsplashImage: UnsplashImage?,
    isFavorite: Boolean,
    onClick: (imageId: String) -> Unit,
    onToggleFavorite: () -> Unit,
    onImageCardDragStart: (previewImage: UnsplashImage?) -> Unit,
    onImageCardDragEnd: () -> Unit,
    onImageCardDragCancel: () -> Unit,
) {
    val context = LocalContext.current
    val imageCardAspectRadio by remember {
        derivedStateOf {
            ((unsplashImage?.width ?: 1).toFloat() / (unsplashImage?.height ?: 1).toFloat())
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
            unsplashImage?.let {
                onClick(it.id)
            }
        },
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(),
                model = ImageRequest
                    .Builder(context)
                    .data(unsplashImage?.imageUrlSmall)
                    .crossfade(true)
                    .build(),
                contentDescription = unsplashImage?.description,
                contentScale = ContentScale.FillBounds,
            )
            FavoriteButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                isFavorite = isFavorite,
                onClick = onToggleFavorite
            )
        }
    }
}

@Composable
fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean,
    onClick: () -> Unit
) {
    IconToggleButton(
        modifier = modifier,
        checked = isFavorite,
        onCheckedChange = {
            onClick()
        }
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.Favorite,
            contentDescription = stringResource(R.string.favorite)
        )
    }
}