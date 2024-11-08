package com.github.bigfishcat.livingpictures.ui

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.domain.BitmapFactory
import com.github.bigfishcat.livingpictures.domain.GifGenerator
import com.github.bigfishcat.livingpictures.domain.PagesRepository
import com.github.bigfishcat.livingpictures.domain.shareFile
import com.github.bigfishcat.livingpictures.model.AppUiState
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.model.PageUiState
import com.github.bigfishcat.livingpictures.model.PopupShown
import com.github.bigfishcat.livingpictures.model.createBottomBarState
import com.github.bigfishcat.livingpictures.model.createTopBarUiState
import com.github.bigfishcat.livingpictures.model.handleAction
import com.github.bigfishcat.livingpictures.ui.bar.BottomBar
import com.github.bigfishcat.livingpictures.ui.bar.TopBar
import com.github.bigfishcat.livingpictures.ui.popup.BrushStrokeWidthPicker
import com.github.bigfishcat.livingpictures.ui.popup.EraserStrokeWidthPicker
import com.github.bigfishcat.livingpictures.ui.popup.ExportToGifPopup
import com.github.bigfishcat.livingpictures.ui.popup.FigurePicker
import com.github.bigfishcat.livingpictures.ui.popup.HueColorPicker
import com.github.bigfishcat.livingpictures.ui.popup.LongProgressPopup
import com.github.bigfishcat.livingpictures.ui.popup.PaletteColorPicker
import com.github.bigfishcat.livingpictures.ui.popup.PencilStrokeWidthPicker
import com.github.bigfishcat.livingpictures.ui.popup.PlayDuePickerDialog
import com.github.bigfishcat.livingpictures.ui.popup.PreviewListPopup
import com.github.bigfishcat.livingpictures.ui.theme.Background
import kotlinx.coroutines.delay

@Composable
fun LivingPicturesApp(
    context: Context,
    modifier: Modifier = Modifier
) {
    val pagesRepository = remember {
        PagesRepository()
    }

    val gifGenerator = remember {
        GifGenerator(context)
    }

    val bitmapFactory = remember {
        BitmapFactory()
    }

    val appState = remember {
        mutableStateOf(AppUiState(currentPageState = pagesRepository.lastPage))
    }

    val topBarState = remember {
        mutableStateOf(appState.value.createTopBarUiState())
    }

    val bottomBarState = remember {
        mutableStateOf(appState.value.createBottomBarState())
    }

    val playbackInProgress = remember {
        mutableStateOf(appState.value.playbackInProgress)
    }

    val bottomBarSize = remember {
        mutableStateOf(IntSize.Zero)
    }

    val canvasSize = remember {
        mutableStateOf(Size.Zero)
    }

    val canvasBackground = remember {
        lazy {
            ImageBitmap.imageResource(context.resources, R.drawable.background)
        }
    }

    val coroutineScope = rememberCoroutineScope()

    fun updateState(uiState: AppUiState) {
        appState.value = uiState
        bottomBarState.value = uiState.createBottomBarState()
        topBarState.value = uiState.createTopBarUiState()
        playbackInProgress.value = uiState.playbackInProgress
    }

    fun updatePage(pageUiState: PageUiState) {
        pagesRepository.update(pageUiState)
        updateState(appState.value.copy(currentPageState = pageUiState))
    }

    fun handleAction(intent: Intent) =
        coroutineScope.handleAction(
            intent = intent,
            context = context,
            appState = appState.value,
            pagesRepository = pagesRepository,
            canvasSize = canvasSize.value,
            updateAppState = ::updateState
        )

    suspend fun drawToBitmap(page: PageUiState): ImageBitmap? {
        val size = canvasSize.value
        if (size.isEmpty()) {
            return null
        }
        return bitmapFactory.drawToBitmap(page, canvasBackground.value, size)
    }

    suspend fun exportToGif(progress: (Float) -> Unit) {
        val pages = pagesRepository.pages

        var index = 0
        progress.invoke(0f)

        gifGenerator.saveToFile(pages, appState.value.playbackDelay.toInt()) { page ->
            drawToBitmap(page).also {
                index++
                progress.invoke((index.toFloat() / pages.size).coerceAtMost(1f))
            }?.asAndroidBitmap()
        }?.let { file ->
            Log.d("EXPORT", "Export to file ${file.canonicalPath}")
            context.shareFile(file)
        }
    }

    Surface(color = Background) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(color = Background),
            topBar = {
                TopBar(
                    uiState = topBarState,
                    modifier = modifier.background(color = Background),
                    action = ::handleAction
                )
            },
            bottomBar = {
                BottomBar(
                    uiState = bottomBarState,
                    modifier = modifier
                        .background(color = Background)
                        .onGloballyPositioned { coordinates ->
                            bottomBarSize.value = coordinates.size
                        },
                    action = ::handleAction
                )
            }
        ) { innerPadding ->
            DrawingPage(
                modifier = modifier
                    .padding(innerPadding)
                    .background(color = Background)
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        canvasSize.value = coordinates.size.toSize()
                    },
                pageUiState = appState.value.currentPageState,
                paintProperties = appState.value.paintProperties,
                enabled = !playbackInProgress.value && appState.value.popupShown != PopupShown.ExportToGif,
                ::updatePage
            )

            when (appState.value.popupShown) {
                PopupShown.None -> {}
                PopupShown.PaletteColorPicker -> PaletteColorPicker(
                    bottomBarSize.value.height,
                    ::handleAction
                )

                PopupShown.HueColorPicker -> HueColorPicker(
                    bottomBarSize.value.height,
                    appState.value.paintProperties.color,
                    ::handleAction
                )

                PopupShown.FiguresPicker -> FigurePicker(bottomBarSize.value.height, ::handleAction)
                PopupShown.PagesPreview -> PreviewListPopup(
                    appState.value,
                    pagesRepository.pages,
                    ::drawToBitmap,
                    ::handleAction
                )

                PopupShown.ExportToGif -> ExportToGifPopup(::exportToGif, ::handleAction)
                PopupShown.LongProgress -> LongProgressPopup()
                PopupShown.DuePicker -> PlayDuePickerDialog(appState.value, ::handleAction)
                PopupShown.PencilStrokeWidthPicker -> PencilStrokeWidthPicker(
                    bottomBarSize.value.height,
                    appState.value,
                    ::handleAction
                )

                PopupShown.BrushStrokeWidthPicker -> BrushStrokeWidthPicker(
                    bottomBarSize.value.height,
                    appState.value,
                    ::handleAction
                )

                PopupShown.EraserStrokeWidthPicker -> EraserStrokeWidthPicker(
                    bottomBarSize.value.height,
                    appState.value,
                    ::handleAction
                )
            }

            BackHandler(enabled = appState.value.popupShown != PopupShown.None) {
                when (appState.value.popupShown) {
                    PopupShown.LongProgress -> {}
                    else -> updateState(appState.value.copy(popupShown = PopupShown.None))
                }
            }

            LaunchedEffect(playbackInProgress.value) {
                if (!playbackInProgress.value) {
                    updatePage(pagesRepository.lastPage)
                    return@LaunchedEffect
                }

                var iterator = pagesRepository.pages.iterator()
                if (!iterator.hasNext()) {
                    Log.e("PLAYBACK", "Nothing to playback")
                    updateState(uiState = appState.value.copy(playbackInProgress = false))
                }

                while (playbackInProgress.value) {
                    if (iterator.hasNext()) {
                        updatePage(iterator.next())
                        delay(appState.value.playbackDelay)
                    } else {
                        iterator = pagesRepository.pages.iterator()
                    }
                }
            }
        }
    }
}