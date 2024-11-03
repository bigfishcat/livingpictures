package com.github.bigfishcat.livingpictures.model

import androidx.compose.ui.graphics.Color
import com.github.bigfishcat.livingpictures.ui.theme.Blue

data class AppUiState(
    val paintProperties: PaintProperties = PaintProperties(),
    val popupShown: PopupShown = PopupShown.None,
    val playbackInProgress: Boolean = false,
    val playbackDelay: Long = 1000,
    val currentPageState: PageUiState = PageUiState()
)

data class PaintProperties(
    val color: Color = Blue,
    val instrument: Instrument = Instrument.Pencil,
    val brushStrokeWidth: BrushStrokeWidth = BrushStrokeWidth.S3,
    val eraserStrokeWidth: BrushStrokeWidth = BrushStrokeWidth.S3,
    val pencilStrokeWidth: PencilStrokeWidth = PencilStrokeWidth.Medium
)