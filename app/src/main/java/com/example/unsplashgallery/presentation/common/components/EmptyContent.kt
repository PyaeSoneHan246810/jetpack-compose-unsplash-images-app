package com.example.unsplashgallery.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    message: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier
                .size(80.dp),
            imageVector = imageVector,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(
            modifier = Modifier.height(12.dp)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}