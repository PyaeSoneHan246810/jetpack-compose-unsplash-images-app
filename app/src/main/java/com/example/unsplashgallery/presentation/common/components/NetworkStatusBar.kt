package com.example.unsplashgallery.presentation.common.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun NetworkStatusBar(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    backgroundColor: Color,
    message: String
) {
    AnimatedVisibility(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        visible = isVisible,
        enter = slideInVertically(
            animationSpec = tween(
                durationMillis = 600
            ),
            initialOffsetY = {
                it / 2
            }
        ),
        exit = slideOutVertically(
            animationSpec = tween(
                durationMillis = 600
            ),
            targetOffsetY = {
                it / 2
            }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .clip(RoundedCornerShape(4.dp))
                .padding(
                    horizontal = 4.dp,
                    vertical = 8.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}