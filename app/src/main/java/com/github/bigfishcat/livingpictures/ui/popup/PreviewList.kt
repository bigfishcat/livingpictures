package com.github.bigfishcat.livingpictures.ui.popup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.model.PageUiState
import com.github.bigfishcat.livingpictures.ui.theme.Active
import com.github.bigfishcat.livingpictures.ui.theme.Background
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme
import com.github.bigfishcat.livingpictures.ui.theme.PopupStroke

@Composable
fun PreviewListPopup(
    pages: List<PageUiState>,
    action: (Intent) -> Unit = {}
) {
    Popup(
        alignment = Alignment.TopCenter,
        offset = IntOffset(0, 0),
        onDismissRequest = { action(Intent.HidePopup) }
    ) {
        PreviewListWithButtons(pages, action)
    }
}

@Composable
fun PreviewListWithButtons(
    pages: List<PageUiState>,
    action: (Intent) -> Unit = {}
) {
    Card(
        modifier = Modifier.shadow(1.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, PopupStroke),
        colors = CardColors(Background, Background, Background, Background)
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            PreviewList(Modifier.weight(1.0f), pages)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Background),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = { action.invoke(Intent.Share) }
                ) {
                    Image(
                        imageVector = Icons.Filled.Share,
                        contentDescription = stringResource(id = R.string.share),
                        colorFilter = ColorFilter.tint(Active)
                    )
                }

                Spacer(modifier = Modifier.width(48.dp))

                IconButton(
                    onClick = { action.invoke(Intent.HidePopup) }
                ) {
                    Image(
                        imageVector = Icons.Filled.Done,
                        contentDescription = stringResource(id = R.string.done),
                        colorFilter = ColorFilter.tint(Active)
                    )
                }
            }
        }
    }
}

@Composable
fun PreviewList(
    modifier: Modifier = Modifier,
    pages: List<PageUiState>
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2)
    ) {
        items(pages) { page ->
            PagePreview(page)
        }
    }
}

@Composable
fun PagePreview(page: PageUiState) {
    Image(
        bitmap = ImageBitmap.imageResource(R.drawable.background),
        contentDescription = null,
        modifier = Modifier
            .padding(6.dp)
            .fillMaxWidth(),
        contentScale = ContentScale.FillWidth
    )
}

@Preview
@Composable
fun DefaultPreviewList() {
    LivingPicturesTheme {
        PreviewList(
            pages = listOf(
                PageUiState(),
                PageUiState(),
                PageUiState(),
                PageUiState(),
                PageUiState(),
                PageUiState(),
            )
        )
    }
}

@Preview
@Composable
fun DefaultPreviewListWithButtons() {
    LivingPicturesTheme {
        PreviewListWithButtons(
            pages = listOf(
                PageUiState(),
                PageUiState(),
            )
        )
    }
}
