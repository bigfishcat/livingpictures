package com.github.bigfishcat.livingpictures.model

import androidx.compose.ui.graphics.Color

sealed interface Intent {
    data object ShowPaletteColorPicker : Intent

    data object ShowWheelColorPicker : Intent

    data object ShowFiguresPicker : Intent

    data object ShowPagesPreview : Intent

    data object ShowPlayDuePicker : Intent

    data class SelectInstrument(val instrument: Instrument) : Intent

    data class SelectColor(val color: Color) : Intent

    data class SelectBrushStrokeWidth(val strokeWidth: BrushStrokeWidth) : Intent

    data class SelectEraserStrokeWidth(val strokeWidth: BrushStrokeWidth) : Intent

    data class SelectPencilStrokeWidth(val strokeWidth: PencilStrokeWidth) : Intent

    data object Undo : Intent

    data object Redo : Intent

    data class Play(val delay: Long) : Intent

    data object Pause : Intent

    data object DeletePage : Intent

    data object DeleteAll : Intent

    data object CreatePage : Intent

    data object CopyPage : Intent

    data class SelectPage(val page: PageUiState) : Intent

    data class GeneratePages(val count: Int) : Intent

    data object HidePopup : Intent

    data class Share(val delay: Long) : Intent
}