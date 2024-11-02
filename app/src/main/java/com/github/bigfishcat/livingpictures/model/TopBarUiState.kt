package com.github.bigfishcat.livingpictures.model

data class TopBarUiState(
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val playbackInProgress: Boolean = false,
    val enabled: Boolean = true
)

fun AppUiState.createTopBarUiState() = TopBarUiState(
    canUndo = !playbackInProgress && currentPageState.objects.isNotEmpty(),
    canRedo = !playbackInProgress && currentPageState.undoObjects.isNotEmpty(),
    playbackInProgress = playbackInProgress,
    enabled = popupShown != PopupShown.ExportToGif && popupShown != PopupShown.PagesPreview
)