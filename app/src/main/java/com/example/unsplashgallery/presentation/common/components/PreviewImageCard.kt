package com.example.unsplashgallery.presentation.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.unsplashgallery.R
import com.example.unsplashgallery.domain.model.UnsplashImage

@Composable
fun PreviewImageCard(
    modifier: Modifier = Modifier,
    previewImage: UnsplashImage?,
) {
    val context = LocalContext.current
    val imageAspectRadio by remember {
        derivedStateOf {
            ((previewImage?.width ?: 1).toFloat() / (previewImage?.height ?: 1).toFloat())
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 8.dp,
                        vertical = 12.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    model = ImageRequest
                        .Builder(context)
                        .data(previewImage?.photographerProfileImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = previewImage?.photographerName,
                    contentScale = ContentScale.FillBounds,
                )
                Spacer(
                    modifier = Modifier
                        .width(12.dp)
                )
                Text(
                    text = previewImage?.photographerName ?: stringResource(R.string.unknown_name)
                )
            }
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(imageAspectRadio),
                model = ImageRequest
                    .Builder(context)
                    .data(previewImage?.imageUrlRegular)
                    .crossfade(true)
                    .build(),
                contentDescription = previewImage?.description,
                contentScale = ContentScale.FillBounds,
            )
        }
    }
}