package com.github.bigfishcat.livingpictures.model

import androidx.compose.ui.graphics.Color
import kotlinx.parcelize.RawValue

data class AppUiState(
    val color: @RawValue Color = Color.White,
    val instrument: Instrument = Instrument.Pencil,
    val popupShown: PopupShown = PopupShown.None,
    val playbackInProgress: Boolean = false,
    val currentPageState: PageUiState = PageUiState()
)