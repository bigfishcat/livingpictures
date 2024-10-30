package com.github.bigfishcat.livingpictures.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class BottomBarUiState(
    val color: @RawValue Color = Color.Blue,
    val selectedItem: BottomItem = BottomItem.Pencil,
) : Parcelable

enum class BottomItem {
    Pencil,
    Brush,
    Eraser,
    Figures,
    Color
}

fun AppUiState.createBottomBarState(): BottomBarUiState {
    val selectedItem = when (popupShown) {
        PopupShown.PagesPreview, PopupShown.None -> {
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

    return BottomBarUiState(color, selectedItem)
}