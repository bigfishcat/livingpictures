package com.github.bigfishcat.livingpictures.ui.bar

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.model.BottomBarUiState
import com.github.bigfishcat.livingpictures.model.BottomItem
import com.github.bigfishcat.livingpictures.model.Instrument
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme
import com.github.bigfishcat.livingpictures.ui.theme.Selected

@Composable
fun BottomBar(
    uiState: State<BottomBarUiState>,
    modifier: Modifier = Modifier,
    action: (Intent) -> Unit = {}
) {
    val selectedItem = uiState.value.selectedItem
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        IconButton(
            onClick = { action(Intent.SelectInstrument(Instrument.Pencil)) }
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.pencil),
                contentDescription = stringResource(id = R.string.pencil),
                colorFilter = if (selectedItem == BottomItem.Pencil) ColorFilter.tint(Selected) else null
            )
        }
        IconButton(
            onClick = { action(Intent.SelectInstrument(Instrument.Brush)) }
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.brush),
                contentDescription = stringResource(id = R.string.brush),
                colorFilter = if (selectedItem == BottomItem.Brush) ColorFilter.tint(Selected) else null
            )
        }
        IconButton(
            onClick = { action(Intent.SelectInstrument(Instrument.Eraser)) }
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.erase),
                contentDescription = stringResource(id = R.string.eraser),
                colorFilter = if (selectedItem == BottomItem.Eraser) ColorFilter.tint(Selected) else null
            )
        }
        IconButton(
            onClick = { action(Intent.ShowFiguresPicker) }
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.instruments),
                contentDescription = stringResource(id = R.string.instruments),
                colorFilter = if (selectedItem == BottomItem.Figures) ColorFilter.tint(Selected) else null
            )
        }
        IconButton(
            onClick = { action(Intent.ShowPaletteColorPicker) }
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.circle),
                contentDescription = stringResource(id = R.string.color),
                colorFilter = if (selectedItem == BottomItem.Color) ColorFilter.tint(Selected) else null,
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
