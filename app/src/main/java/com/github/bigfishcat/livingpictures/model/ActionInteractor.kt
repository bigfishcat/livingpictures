package com.github.bigfishcat.livingpictures.model

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.geometry.Size
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.domain.PagesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun CoroutineScope.handleAction(
    intent: Intent,
    context: Context,
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
            Toast.makeText(context, R.string.page_created, Toast.LENGTH_SHORT).show()
        }

        Intent.CopyPage -> {
            updateAppState.invoke(state.copy(currentPageState = pagesRepository.clone(state.currentPageState)))
            Toast.makeText(context, R.string.page_copied, Toast.LENGTH_SHORT).show()
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
            generatePages(state, intent, updateAppState, pagesRepository, canvasSize)
        }

        Intent.Pause -> {
            updateAppState.invoke(state.copy(playbackInProgress = false))
        }

        is Intent.Play -> {
            updateAppState.invoke(state.copy(playbackInProgress = true, playbackDelay = intent.delay))
        }

        Intent.Undo -> {
            updateAppState.invoke(state.copy(currentPageState = state.currentPageState.undo()))
        }

        Intent.Redo -> {
            updateAppState.invoke(state.copy(currentPageState = state.currentPageState.redo()))
        }

        is Intent.SelectColor -> {
            updateAppState.invoke(state.copy(paintProperties = state.paintProperties.copy(color = intent.color)))
        }

        is Intent.SelectInstrument -> {
            val popup = when(intent.instrument) {
                Instrument.Pencil -> PopupShown.PencilStrokeWidthPicker
                Instrument.Brush -> PopupShown.BrushStrokeWidthPicker
                Instrument.Eraser -> PopupShown.EraserStrokeWidthPicker
                else -> PopupShown.None
            }
            updateAppState.invoke(state.copy(
                paintProperties = state.paintProperties.copy(instrument = intent.instrument),
                popupShown = popup
            ))
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
            updateAppState.invoke(state.copy(popupShown = PopupShown.HueColorPicker))
        }

        Intent.ShowPlayDuePicker -> {
            updateAppState.invoke(state.copy(popupShown = PopupShown.DuePicker))
        }

        is Intent.Share -> {
            updateAppState.invoke(state.copy(popupShown = PopupShown.ExportToGif, playbackDelay = intent.delay))
        }

        is Intent.SelectBrushStrokeWidth -> {
            updateAppState.invoke(state.copy(paintProperties = state.paintProperties.copy(brushStrokeWidth = intent.strokeWidth)))
        }

        is Intent.SelectEraserStrokeWidth -> {
            updateAppState.invoke(state.copy(paintProperties = state.paintProperties.copy(eraserStrokeWidth = intent.strokeWidth)))
        }

        is Intent.SelectPencilStrokeWidth -> {
            updateAppState.invoke(state.copy(paintProperties = state.paintProperties.copy(pencilStrokeWidth = intent.strokeWidth)))
        }
    }
}

private fun CoroutineScope.generatePages(
    state: AppUiState,
    intent: Intent.GeneratePages,
    updateAppState: (AppUiState) -> Unit,
    pagesRepository: PagesRepository,
    canvasSize: Size
) {
    var updatedState = state
    if (intent.count > 100) {
        updatedState = updatedState.copy(popupShown = PopupShown.LongProgress)
        updateAppState.invoke(updatedState)
    }
    launch {
        for (i in 0 until intent.count) {
            val page = pagesRepository.create().copy(
                objects = generateRandomObjects(canvasSize)
            )
            pagesRepository.update(page)
            updatedState = updatedState.copy(currentPageState = page)
            updateAppState.invoke(updatedState)
            if (intent.count < 100) {
                delay(30)
            } else if (intent.count < 1000) {
                delay(3)
            }
        }

        if (intent.count > 100) {
            updateAppState.invoke(updatedState.copy(popupShown = PopupShown.None))
        }
    }
}