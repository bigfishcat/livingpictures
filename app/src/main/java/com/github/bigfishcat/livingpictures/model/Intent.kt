package com.github.bigfishcat.livingpictures.model

import androidx.compose.ui.graphics.Color

sealed interface Intent {
    data object ShowPaletteColorPicker : Intent

    data object ShowWheelColorPicker : Intent

    data object ShowFiguresPicker : Intent

    data object ShowPagesPreview : Intent

    data class SelectInstrument(val instrument: Instrument) : Intent

    data class SelectColor(val color: Color) : Intent

    data object Undo : Intent

    data object Redo : Intent

    data object Play : Intent

    data object Pause : Intent

    data object DeletePage : Intent

    data object CreatePage : Intent
}