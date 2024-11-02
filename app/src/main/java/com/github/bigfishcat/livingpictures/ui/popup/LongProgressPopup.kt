package com.github.bigfishcat.livingpictures.ui.popup

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme

@Composable
fun LongProgressPopup() {
    Popup(
        alignment = Alignment.Center,
        offset = IntOffset.Zero,
        onDismissRequest = { },
        properties = PopupProperties(
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        )
    ) {
        LongProgress()
    }
}

@Composable
private fun LongProgress() {
    PopupContentContainer {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
private fun DefaultLongProgress() {
    LivingPicturesTheme {
        LongProgress()
    }
}