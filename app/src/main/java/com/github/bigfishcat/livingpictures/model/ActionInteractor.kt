package com.github.bigfishcat.livingpictures.model

import androidx.compose.ui.geometry.Size
import com.github.bigfishcat.livingpictures.domain.PagesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun CoroutineScope.handleAction(
    intent: Intent,
    appState: AppUiState,
    pagesRepository: PagesRepository,
    canvasSize: Size,
    updateAppState: (AppUiState) -> Unit
) {
    val state = appState.copy(popupShown = PopupShown.None)
    when (intent) {
        Intent.HidePopup -> {
            updateAppState.invoke(state)
        }

        Intent.CreatePage -> {
            updateAppState.invoke(state.copy(currentPageState = pagesRepository.create()))
        }

        Intent.CopyPage -> {
            updateAppState.invoke(state.copy(currentPageState = pagesRepository.clone(state.currentPageState)))
        }

        Intent.DeletePage -> {
            updateAppState.invoke(state.copy(currentPageState = pagesRepository.remove(state.currentPageState)))
        }

        Intent.DeleteAll -> {
            updateAppState.invoke(state.copy(currentPageState = pagesRepository.removeAll()))
        }

        is Intent.SelectPage -> {
            updateAppState.invoke(state.copy(currentPageState = intent.page))
        }

        is Intent.GeneratePages -> {
            launch {
                for (i in 0 until intent.count) {
                    val page = pagesRepository.create().copy(
                        objects = generateRandomObjects(canvasSize)
                    )
                    pagesRepository.update(page)
                    updateAppState.invoke(state.copy(currentPageState = page))
                    delay(30)
                }
            }
        }

        Intent.Pause -> {
            updateAppState.invoke(state.copy(playbackInProgress = false))
        }

        Intent.Play -> {
            updateAppState.invoke(state.copy(playbackInProgress = true))
        }

        Intent.Undo -> {
            updateAppState.invoke(state.copy(currentPageState = state.currentPageState.undo()))
        }

        Intent.Redo -> {
            updateAppState.invoke(state.copy(currentPageState = state.currentPageState.redo()))
        }

        is Intent.SelectColor -> {
            updateAppState.invoke(state.copy(color = intent.color))
        }

        is Intent.SelectInstrument -> {
            updateAppState.invoke(state.copy(instrument = intent.instrument))
        }

        Intent.ShowFiguresPicker -> {
            updateAppState.invoke(state.copy(popupShown = PopupShown.FiguresPicker))
        }

        Intent.ShowPagesPreview -> {
            updateAppState.invoke(state.copy(popupShown = PopupShown.PagesPreview))
        }

        Intent.ShowPaletteColorPicker -> {
            updateAppState.invoke(state.copy(popupShown = PopupShown.PaletteColorPicker))
        }

        Intent.ShowWheelColorPicker -> {
            updateAppState.invoke(state.copy(popupShown = PopupShown.WheelColorPicker))
        }

        Intent.Share -> updateAppState.invoke(state.copy(popupShown = PopupShown.ExportToGif))
    }
}