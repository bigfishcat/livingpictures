package com.github.bigfishcat.livingpictures.ui.popup

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme

@Composable
fun ExportToGifPopup(
    export: suspend (progress: (Float) -> Unit) -> Unit,
    action: (Intent) -> Unit = {}
) {
    Popup(
        alignment = Alignment.Center,
        offset = IntOffset.Zero,
        onDismissRequest = { action(Intent.HidePopup) },
        properties = PopupProperties(dismissOnClickOutside = false)
    ) {
        ExportToGif(export, action)
    }
}

@Composable
private fun ExportToGif(
    export: suspend (progress: (Float) -> Unit) -> Unit,
    action: (Intent) -> Unit = {}
) {
    PopupContentContainer {
        val currentProgress = remember {
            mutableFloatStateOf(0f)
        }

        LinearProgressIndicator(
            progress = { currentProgress.floatValue },
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .align(Alignment.CenterVertically)
        )

        LaunchedEffect(export) {
            export.invoke { progress ->
                currentProgress.floatValue = progress
            }
            action(Intent.HidePopup)
        }
    }
}

@Preview
@Composable
private fun DefaultExportToGif() {
    LivingPicturesTheme {
        ExportToGif({}, {})
    }
}