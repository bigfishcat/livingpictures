package com.github.bigfishcat.livingpictures.model

fun handleAction(
    intent: Intent,
    appState: AppUiState,
    updateAppState: (AppUiState) -> Unit
) {
    val state = appState.copy(popupShown = PopupShown.None)
    when (intent) {
        Intent.CreatePage -> TODO()
        Intent.DeletePage -> TODO()
        Intent.Pause -> {
            updateAppState.invoke(state.copy(playbackInProgress = false))
        }
        Intent.Play -> {
            updateAppState.invoke(state.copy(playbackInProgress = true))
        }
        Intent.Undo -> TODO()
        Intent.Redo -> TODO()
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
    }
}