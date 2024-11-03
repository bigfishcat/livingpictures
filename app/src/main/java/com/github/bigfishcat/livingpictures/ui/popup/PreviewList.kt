package com.github.bigfishcat.livingpictures.ui.popup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.model.AppUiState
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.model.PageUiState
import com.github.bigfishcat.livingpictures.ui.theme.Active
import com.github.bigfishcat.livingpictures.ui.theme.Background
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme
import com.github.bigfishcat.livingpictures.ui.theme.PopupStroke

private sealed interface ImageState {
    data object Loading : ImageState

    data object Failed : ImageState

    class Success(val painter: Painter) : ImageState
}

@Composable
fun PreviewListPopup(
    appState: AppUiState,
    pages: List<PageUiState>,
    bitmapFactory: suspend (PageUiState) -> ImageBitmap?,
    action: (Intent) -> Unit = {}
) {
    Popup(
        alignment = Alignment.TopCenter,
        offset = IntOffset(0, 0),
        onDismissRequest = { action(Intent.HidePopup) }
    ) {
        PreviewListWithButtons(appState, pages, bitmapFactory, action)
    }
}

@Composable
fun PreviewListWithButtons(
    appState: AppUiState,
    pages: List<PageUiState>,
    bitmapFactory: suspend (PageUiState) -> ImageBitmap?,
    action: (Intent) -> Unit = {}
) {
    Card(
        modifier = Modifier.shadow(1.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, PopupStroke),
        colors = CardColors(Background, Background, Background, Background)
    ) {
        val confirmDeleteAll = remember {
            mutableStateOf(false)
        }

        val confirmGeneratePages = remember {
            mutableStateOf(false)
        }

        val confirmShare = remember {
            mutableStateOf(false)
        }

        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
            PreviewList(Modifier.weight(1.0f), pages, bitmapFactory, action)

            PreviewBottomBar(confirmDeleteAll, confirmGeneratePages, confirmShare, action)
        }

        if (confirmDeleteAll.value) {
            DeleteAllDialog(confirmDeleteAll, action)
        }

        if (confirmGeneratePages.value) {
            GeneratePagesDialog(confirmGeneratePages, action)
        }

        if (confirmShare.value) {
            DuePickerDialog(
                appState.playbackDelay,
                { action.invoke(Intent.Share(it)) },
                { confirmShare.value = false }
            )
        }
    }
}

@Composable
private fun PreviewBottomBar(
    confirmDeleteAll: MutableState<Boolean>,
    confirmGeneratePages: MutableState<Boolean>,
    confirmShare: MutableState<Boolean>,
    action: (Intent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(
            onClick = { confirmDeleteAll.value = true }
        ) {
            Image(
                painter = painterResource(id = R.drawable.delete_all),
                contentDescription = stringResource(id = R.string.delete_all)
            )
        }

        IconButton(
            onClick = { confirmGeneratePages.value = true }
        ) {
            Image(
                painter = painterResource(id = R.drawable.generate_pages),
                contentDescription = stringResource(id = R.string.generate_pages)
            )
        }

        IconButton(
            onClick = { confirmShare.value = true }
        ) {
            Image(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(id = R.string.share),
                colorFilter = ColorFilter.tint(Active)
            )
        }

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

@Composable
private fun DeleteAllDialog(
    confirmDeleteAll: MutableState<Boolean>,
    action: (Intent) -> Unit = {}
) {
    ConfirmDialog(
        iconRes = R.drawable.delete_all,
        titleRes = R.string.delete_all,
        confirm = {
            confirmDeleteAll.value = false
            action.invoke(Intent.DeleteAll)
        },
        dismiss = {
            confirmDeleteAll.value = false
        }
    )
}

@Composable
private fun GeneratePagesDialog(
    confirmGeneratePages: MutableState<Boolean>,
    action: (Intent) -> Unit = {}
) {
    val text = remember { mutableStateOf("1") }
    val pattern = remember { Regex("^\\d+\$") }

    fun confirm() {
        confirmGeneratePages.value = false
        text.value.toIntOrNull()?.let { count ->
            action.invoke(Intent.GeneratePages(count))
        }
    }

    TextFieldDialog(
        iconRes = R.drawable.generate_pages,
        titleRes = R.string.generate_pages,
        labelRes = R.string.count,
        text = text,
        onValueChange = { if (pattern.matches(it)) text.value = it },
        confirm = ::confirm,
        dismiss = { confirmGeneratePages.value = false }
    )
}

@Composable
fun PreviewList(
    modifier: Modifier = Modifier,
    pages: List<PageUiState>,
    bitmapFactory: suspend (PageUiState) -> ImageBitmap?,
    action: (Intent) -> Unit = {}
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2)
    ) {
        items(pages) { page ->
            PagePreview(
                modifier = Modifier.clickable { action.invoke(Intent.SelectPage(page)) },
                page = page,
                bitmapFactory = bitmapFactory
            )
        }
    }
}

@Composable
fun PagePreview(
    modifier: Modifier,
    page: PageUiState,
    bitmapFactory: suspend (PageUiState) -> ImageBitmap?
) {
    val state = loadBitmap(page, bitmapFactory)

    val tileModifier = modifier
        .padding(6.dp)
        .shadow(1.dp)
        .fillMaxWidth()

    when (val imageState = state.value) {
        ImageState.Loading -> {
            Box(
                modifier = tileModifier
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth
                )
                Image(
                    painter = painterResource(id = R.drawable.loading),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }

        ImageState.Failed -> {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = null,
                modifier = tileModifier,
                contentScale = ContentScale.FillWidth
            )
        }

        is ImageState.Success -> {
            Image(
                painter = imageState.painter,
                contentDescription = null,
                modifier = tileModifier,
                contentScale = ContentScale.FillWidth
            )
        }
    }
}

@Composable
private fun loadBitmap(
    page: PageUiState,
    bitmapFactory: suspend (PageUiState) -> ImageBitmap?
): State<ImageState> {
    return produceState<ImageState>(ImageState.Loading, page, bitmapFactory) {
        bitmapFactory.invoke(page)?.let { bitmap ->
            value = ImageState.Success(BitmapPainter(bitmap))
        } ?: run {
            value = ImageState.Failed
        }
    }
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
            ),
            bitmapFactory = { null }
        )
    }
}

@Preview
@Composable
fun DefaultPreviewListWithButtons() {
    LivingPicturesTheme {
        PreviewListWithButtons(
            AppUiState(),
            pages = listOf(
                PageUiState(),
                PageUiState(),
            ),
            bitmapFactory = { null }
        )
    }
}

@Preview
@Composable
fun DefaultDeleteAllDialog() {
    LivingPicturesTheme {
        val confirmDeleteAll = remember {
            mutableStateOf(true)
        }
        DeleteAllDialog(confirmDeleteAll)
    }
}

@Preview
@Composable
fun DefaultGeneratePagesDialog() {
    LivingPicturesTheme {
        val confirmGeneratePages = remember {
            mutableStateOf(true)
        }
        GeneratePagesDialog(confirmGeneratePages)
    }
}
