package com.github.bigfishcat.livingpictures.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TopBarUiState(
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val playbackInProgress: Boolean = false
) : Parcelable