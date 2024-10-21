package com.example.unsplashgallery.presentation.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDownloadOptionsModalBottomSheet(
    modifier: Modifier = Modifier,
    imageDownloadOptions: List<ImageDownloadOption> = ImageDownloadOption.entries,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onDownloadOptionClick: (option: ImageDownloadOption) -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
    ) {
        imageDownloadOptions.forEach { option ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDownloadOptionClick(option)
                    }
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = option.label
                )
            }
        }
    }
}

enum class ImageDownloadOption(
    val label: String
) {
    SMALL_SIZE(label = "Download Small Size"),
    MEDIUM_SIZE(label = "Download Medium Size"),
    ORIGINAL_SIZE(label = "Download Original Size")
}