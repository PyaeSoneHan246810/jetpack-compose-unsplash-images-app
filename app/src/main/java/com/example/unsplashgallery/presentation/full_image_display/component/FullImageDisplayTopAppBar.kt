package com.example.unsplashgallery.presentation.full_image_display.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.unsplashgallery.R
import com.example.unsplashgallery.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FullImageDisplayTopAppBar(
    modifier: Modifier = Modifier,
    photographerProfileImageUrl: String?,
    photographerName: String?,
    photographerUsername: String?,
    photographerProfileLink: String?,
    onArrowBackIconButtonClick: () -> Unit,
    onDownloadIconButtonClick: () -> Unit,
    onPhotographerInfoClick: (profileLink: String) -> Unit
) {
    val context = LocalContext.current
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable {
                            photographerProfileLink?.let {
                                onPhotographerInfoClick(it)
                            }
                        },
                    model = ImageRequest
                        .Builder(context)
                        .data(photographerProfileImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = photographerName,
                )
                Spacer(
                    modifier = Modifier
                        .width(12.dp)
                )
                Column {
                    Text(
                        modifier = Modifier
                            .clickable {
                                photographerProfileLink?.let {
                                    onPhotographerInfoClick(it)
                                }
                            },
                        text = photographerName ?: stringResource(R.string.unknown_name),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        modifier = Modifier
                            .clickable {
                                photographerProfileLink?.let {
                                    onPhotographerInfoClick(it)
                                }
                            },
                        text = photographerUsername ?: stringResource(R.string.unknown_username),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onArrowBackIconButtonClick
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        },
        actions = {
            IconButton(
                onClick = onDownloadIconButtonClick
            ) {
                Icon(
                    imageVector = Icons.Filled.Download,
                    contentDescription = stringResource(R.string.download_image)
                )
            }
        }
    )
}

@Preview
@Composable
private fun FullImageDisplayTopAppBarPrev() {
    AppTheme {
        FullImageDisplayTopAppBar(
            photographerProfileImageUrl = stringResource(R.string.url_placeholder),
            photographerName = stringResource(R.string.unknown_name),
            photographerUsername = stringResource(R.string.unknown_username),
            photographerProfileLink = stringResource(R.string.url_placeholder),
            onArrowBackIconButtonClick = {},
            onDownloadIconButtonClick = {},
            onPhotographerInfoClick = {}
        )
    }
}