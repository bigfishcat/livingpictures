package com.github.bigfishcat.livingpictures.model

data class TopBarUiState(
    val canUndo: Boolean = false,
    val canRedo: Boolean = false,
    val playbackInProgress: Boolean = false
)

fun AppUiState.createTopBarUiState() = TopBarUiState(
    canUndo = currentPageState.objects.isNotEmpty(),
    canRedo = currentPageState.undoObjects.isNotEmpty(),
    playbackInProgress = playbackInProgress
)