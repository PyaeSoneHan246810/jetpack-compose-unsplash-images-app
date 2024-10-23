package com.example.unsplashgallery.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.example.unsplashgallery.domain.model.UnsplashImage

@Composable
fun UnsplashImageCardGrids(
    modifier: Modifier = Modifier,
    bottomContentPadding: Dp = 0.dp,
    unsplashImages: List<UnsplashImage>,
    onImageCardClick: (imageId: String) -> Unit,
    onImageCardDragStart: (previewImage: UnsplashImage?) -> Unit,
    onImageCardDragEnd: () -> Unit,
    onImageCardDragCancel: () -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        columns = StaggeredGridCells.Adaptive(140.dp),
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 12.dp,
            end = 12.dp,
            bottom = bottomContentPadding + 12.dp
        )
    ) {
        items(
            items = unsplashImages,
        ) { unsplashImage ->
            UnsplashImageCard(
                unsplashImage = unsplashImage,
                onClick = onImageCardClick,
                onImageCardDragStart = onImageCardDragStart,
                onImageCardDragEnd = onImageCardDragEnd,
                onImageCardDragCancel = onImageCardDragCancel
            )
        }
    }
}

@Composable
fun UnsplashImageCardGrids(
    modifier: Modifier = Modifier,
    bottomContentPadding: Dp = 0.dp,
    unsplashImages: LazyPagingItems<UnsplashImage>,
    onImageCardClick: (imageId: String) -> Unit,
    onImageCardDragStart: (previewImage: UnsplashImage?) -> Unit,
    onImageCardDragEnd: () -> Unit,
    onImageCardDragCancel: () -> Unit
) {
    LazyVerticalStaggeredGrid(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        columns = StaggeredGridCells.Adaptive(140.dp),
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            start = 12.dp,
            top = 12.dp,
            end = 12.dp,
            bottom = bottomContentPadding + 12.dp
        )
    ) {
        items(
            count = unsplashImages.itemCount,
        ) { index ->
            val unsplashImage = unsplashImages[index]
            UnsplashImageCard(
                unsplashImage = unsplashImage,
                onClick = onImageCardClick,
                onImageCardDragStart = onImageCardDragStart,
                onImageCardDragEnd = onImageCardDragEnd,
                onImageCardDragCancel = onImageCardDragCancel
            )
        }
    }
}