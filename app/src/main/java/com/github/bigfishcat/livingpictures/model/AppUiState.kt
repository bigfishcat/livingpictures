package com.github.bigfishcat.livingpictures.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class AppUiState(
    val color: @RawValue Color = Color.White,
    val instrument: Instrument = Instrument.Pencil,
    val popupShown: PopupShown = PopupShown.None,
    val playbackInProgress: Boolean = false
) : Parcelable