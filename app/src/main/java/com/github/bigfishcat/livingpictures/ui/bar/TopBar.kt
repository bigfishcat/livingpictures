package com.github.bigfishcat.livingpictures.ui.bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.model.TopBarUiState
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme

@Composable
fun TopBar(
    uiState: State<TopBarUiState>,
    modifier: Modifier = Modifier,
    action: (Intent) -> Unit = {}
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        IconButton(
            onClick = { action(Intent.Undo) },
            enabled = uiState.value.canUndo
        ) {
            Image(
                imageVector = ImageVector.vectorResource(
                    id = if (uiState.value.canUndo) R.drawable.undo_active else R.drawable.undo_inactive
                ),
                contentDescription = stringResource(id = R.string.undo)
            )
        }

        IconButton(
            onClick = { action(Intent.Redo) },
            enabled = uiState.value.canRedo
        ) {
            Image(
                imageVector = ImageVector.vectorResource(
                    id = if (uiState.value.canRedo) R.drawable.redo_active else R.drawable.redo_inactive
                ),
                contentDescription = stringResource(id = R.string.redo)
            )
        }

        Spacer(modifier = modifier.weight(weight = 1.0f))

        IconButton(onClick = { action(Intent.DeletePage) }) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.bin),
                contentDescription = stringResource(id = R.string.bin)
            )
        }

        IconButton(onClick = { action(Intent.CreatePage) }) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.resource_new),
                contentDescription = stringResource(id = R.string.add_page)
            )
        }

        IconButton(onClick = { action(Intent.ShowPagesPreview) }) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.layers),
                contentDescription = stringResource(id = R.string.all)
            )
        }

        Spacer(modifier = modifier.weight(weight = 1.0f))

        IconButton(
            onClick = { action(Intent.Pause) },
            enabled = uiState.value.playbackInProgress
        ) {
            Image(
                imageVector = ImageVector.vectorResource(
                    id = if (uiState.value.playbackInProgress) R.drawable.pause_active else R.drawable.pause_inactive
                ),
                contentDescription = stringResource(id = R.string.play)
            )
        }

        IconButton(
            onClick = { action(Intent.Play) },
            enabled = !uiState.value.playbackInProgress
        ) {
            Image(
                imageVector = ImageVector.vectorResource(
                    id = if (uiState.value.playbackInProgress) R.drawable.play_inactive else R.drawable.play_active
                ),
                contentDescription = stringResource(id = R.string.pause)
            )
        }
    }
}

@Preview(backgroundColor = 0xFF000000)
@Composable
fun DefaultTopBar() {
    LivingPicturesTheme {
        val uiState = remember {
            mutableStateOf(TopBarUiState())
        }
        TopBar(uiState)
    }
}

@Preview(backgroundColor = 0xFF000000)
@Composable
fun TopBarWithUndoRedo() {
    LivingPicturesTheme {
        val uiState = remember {
            mutableStateOf(
                TopBarUiState(
                    canRedo = true,
                    canUndo = true,
                    playbackInProgress = true
                )
            )
        }
        TopBar(uiState)
    }
}