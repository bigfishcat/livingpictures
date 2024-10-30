package com.github.bigfishcat.livingpictures.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme

@Composable
fun DrawingPage(modifier: Modifier = Modifier) {
    Image(
        bitmap = ImageBitmap.imageResource(R.drawable.background),
        contentDescription = null,
        modifier = modifier.padding(6.dp),
        contentScale = ContentScale.FillBounds
    )
}

@Preview
@Composable
fun DefaultDrawingPage() {
    LivingPicturesTheme {
        DrawingPage()
    }
}