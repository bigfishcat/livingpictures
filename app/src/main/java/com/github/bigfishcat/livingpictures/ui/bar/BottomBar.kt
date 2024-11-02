package com.github.bigfishcat.livingpictures.ui.bar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.model.BottomBarUiState
import com.github.bigfishcat.livingpictures.model.BottomItem
import com.github.bigfishcat.livingpictures.model.Instrument
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.ui.theme.Inactive
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme
import com.github.bigfishcat.livingpictures.ui.theme.Selected

@Composable
fun BottomBar(
    uiState: State<BottomBarUiState>,
    modifier: Modifier = Modifier,
    action: (Intent) -> Unit = {}
) {
    val selectedItem = uiState.value.selectedItem
    val enabled = uiState.value.enabled

    fun colorFilter(item: BottomItem): ColorFilter? {
        return when {
            !enabled -> ColorFilter.tint(Inactive)
            item == selectedItem -> ColorFilter.tint(Selected)
            else -> null
        }
    }

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        IconButton(
            onClick = { action(Intent.SelectInstrument(Instrument.Pencil)) },
            enabled = enabled
        ) {
            Image(
                painter = painterResource(id = R.drawable.pencil),
                contentDescription = stringResource(id = R.string.pencil),
                colorFilter = colorFilter(BottomItem.Pencil)
            )
        }
        IconButton(
            onClick = { action(Intent.SelectInstrument(Instrument.Brush)) },
            enabled = enabled
        ) {
            Image(
                painter = painterResource(id = R.drawable.brush),
                contentDescription = stringResource(id = R.string.brush),
                colorFilter = colorFilter(BottomItem.Brush)
            )
        }
        IconButton(
            onClick = { action(Intent.SelectInstrument(Instrument.Eraser)) },
            enabled = enabled
        ) {
            Image(
                painter = painterResource(id = R.drawable.erase),
                contentDescription = stringResource(id = R.string.eraser),
                colorFilter = colorFilter(BottomItem.Eraser)
            )
        }
        IconButton(
            onClick = { action(Intent.ShowFiguresPicker) },
            enabled = enabled
        ) {
            Image(
                painter = painterResource(id = R.drawable.instruments),
                contentDescription = stringResource(id = R.string.instruments),
                colorFilter = colorFilter(BottomItem.Figures)
            )
        }
        IconButton(
            onClick = { action(Intent.ShowPaletteColorPicker) },
            enabled = enabled
        ) {
            Image(
                painter = painterResource(id = R.drawable.circle),
                contentDescription = stringResource(id = R.string.color),
                colorFilter = colorFilter(BottomItem.Color),
                modifier = Modifier.drawBehind {
                    drawCircle(
                        color = uiState.value.color,
                        radius = size.minDimension / 2.5f
                    )
                }
            )
        }
    }
}

@Preview(backgroundColor = 0xFF000000)
@Composable
private fun DefaultBottomBar() {
    LivingPicturesTheme {
        val uiState = remember {
            mutableStateOf(BottomBarUiState())
        }
        BottomBar(uiState)
    }
}
