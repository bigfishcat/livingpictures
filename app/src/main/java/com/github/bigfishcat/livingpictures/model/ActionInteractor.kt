package com.github.bigfishcat.livingpictures.model

import com.github.bigfishcat.livingpictures.domain.PagesRepository

fun handleAction(
    intent: Intent,
    appState: AppUiState,
    pagesRepository: PagesRepository,
    updateAppState: (AppUiState) -> Unit
) {
    val state = appState.copy(popupShown = PopupShown.None)
    when (intent) {
        Intent.HidePopup -> {
            updateAppState.invoke(state)
        }
        Intent.CreatePage -> {
            pagesRepository.push(state.currentPageState)
            updateAppState.invoke(state.copy(currentPageState = PageUiState()))
        }

        Intent.DeletePage -> {
            val pageState = pagesRepository.pop() ?: PageUiState()
            updateAppState.invoke(state.copy(currentPageState = pageState))
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

        Intent.Share -> TODO()
    }
}