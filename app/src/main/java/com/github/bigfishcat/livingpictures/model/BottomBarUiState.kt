package com.github.bigfishcat.livingpictures.model

import androidx.compose.ui.graphics.Color

data class BottomBarUiState(
    val color: Color = Color.Blue,
    val selectedItem: BottomItem = BottomItem.Pencil,
    val enabled: Boolean = true
)

enum class BottomItem {
    Pencil,
    Brush,
    Eraser,
    Figures,
    Color
}

fun AppUiState.createBottomBarState(): BottomBarUiState {
    val selectedItem = when (popupShown) {
        PopupShown.PagesPreview, PopupShown.ExportToGif, PopupShown.None -> {
            when (instrument) {
                Instrument.Pencil -> BottomItem.Pencil
                Instrument.Brush -> BottomItem.Brush
                Instrument.Eraser -> BottomItem.Eraser
                Instrument.Triangle,
                Instrument.Rectangle,
                Instrument.Circle,
                Instrument.Arrow -> BottomItem.Figures
            }
        }

        PopupShown.PaletteColorPicker -> BottomItem.Color
        PopupShown.WheelColorPicker -> BottomItem.Color
        PopupShown.FiguresPicker -> BottomItem.Figures
    }

    return BottomBarUiState(
        color = color,
        selectedItem = selectedItem,
        enabled = !playbackInProgress && popupShown != PopupShown.ExportToGif
    )
}