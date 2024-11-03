package com.github.bigfishcat.livingpictures.ui.popup

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.model.Instrument
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme

private class InstrumentModel(
    val type: Instrument,
    @DrawableRes
    val iconRes: Int,
    @StringRes
    val description: Int
) {
    companion object {
        val Instruments = listOf(
            InstrumentModel(Instrument.Circle, R.drawable.circle, R.string.circle),
            InstrumentModel(Instrument.Rectangle, R.drawable.square, R.string.square),
            InstrumentModel(Instrument.Triangle, R.drawable.triangle, R.string.triangle),
            InstrumentModel(Instrument.Arrow, R.drawable.arrow, R.string.arrow)
        )
    }
}

@Composable
fun FigurePicker(bottomBarHeight: Int, action: (Intent) -> Unit = {}) {
    Popup(
        alignment = Alignment.BottomCenter,
        offset = IntOffset(0, -bottomBarHeight),
        onDismissRequest = { action(Intent.HidePopup) }
    ) {
        FigurePickerContent(action)
    }
}

@Composable
private fun FigurePickerContent(action: (Intent) -> Unit) {
    PopupContentContainer {
        InstrumentModel.Instruments.forEach { model ->
            IconButton(
                onClick = { action(Intent.SelectInstrument(model.type)) }
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = model.iconRes),
                    contentDescription = stringResource(id = model.description)
                )
            }
        }
    }
}

@Preview
@Composable
private fun DefaultFigurePickerContent() {
    LivingPicturesTheme {
        FigurePickerContent {}
    }
}