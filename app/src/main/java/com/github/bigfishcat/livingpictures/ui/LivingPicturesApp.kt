package com.github.bigfishcat.livingpictures.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.toSize
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.domain.BitmapFactory
import com.github.bigfishcat.livingpictures.domain.PagesRepository
import com.github.bigfishcat.livingpictures.model.AppUiState
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.model.PageUiState
import com.github.bigfishcat.livingpictures.model.PopupShown
import com.github.bigfishcat.livingpictures.model.createBottomBarState
import com.github.bigfishcat.livingpictures.model.createTopBarUiState
import com.github.bigfishcat.livingpictures.model.handleAction
import com.github.bigfishcat.livingpictures.ui.bar.BottomBar
import com.github.bigfishcat.livingpictures.ui.bar.TopBar
import com.github.bigfishcat.livingpictures.ui.popup.FigurePicker
import com.github.bigfishcat.livingpictures.ui.popup.PaletteColorPicker
import com.github.bigfishcat.livingpictures.ui.popup.PreviewListPopup
import com.github.bigfishcat.livingpictures.ui.theme.Background
import kotlinx.coroutines.CoroutineScope

@Composable
fun LivingPicturesApp(
    context: Context,
    coroutineScope: CoroutineScope,
    modifier: Modifier = Modifier
) {
    val pagesRepository = remember {
        PagesRepository(context, coroutineScope)
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

    val canvasSize = remember {
        mutableStateOf(Size.Zero)
    }

    val canvasBackground = remember {
        lazy {
            ImageBitmap.imageResource(context.resources, R.drawable.background)
        }
    }

    fun updateState(uiState: AppUiState) {
        appState.value = uiState
        bottomBarState.value = uiState.createBottomBarState()
        topBarState.value = uiState.createTopBarUiState()
    }

    fun updatePage(pageUiState: PageUiState) {
        pagesRepository.update(pageUiState)
        updateState(appState.value.copy(currentPageState = pageUiState))
    }

    fun handleAction(intent: Intent) =
        handleAction(intent, appState.value, pagesRepository, ::updateState)

    suspend fun drawToBitmap(page: PageUiState): ImageBitmap? {
        val size = canvasSize.value
        if (size.isEmpty() || page.objects.isEmpty()) {
            return null
        }
        return bitmapFactory.drawToBitmap(page, canvasBackground.value, size)
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .background(color = Background),
            topBar = { TopBar(topBarState, modifier, ::handleAction) },
            bottomBar = { BottomBar(bottomBarState, modifier, ::handleAction) }
        ) { innerPadding ->
            DrawingPage(
                modifier = modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .onGloballyPositioned { coordinates ->
                        canvasSize.value = coordinates.size.toSize()
                    },
                pageUiState = appState.value.currentPageState,
                selectedInstrument = appState.value.instrument,
                color = appState.value.color,
                ::updatePage
            )

            when (appState.value.popupShown) {
                PopupShown.None -> {}
                PopupShown.PaletteColorPicker -> PaletteColorPicker(::handleAction)
                PopupShown.WheelColorPicker -> TODO()
                PopupShown.FiguresPicker -> FigurePicker(::handleAction)
                PopupShown.PagesPreview -> PreviewListPopup(
                    pagesRepository.pages,
                    ::drawToBitmap,
                    ::handleAction
                )
            }
        }
    }
}