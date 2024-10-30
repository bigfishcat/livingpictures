package com.github.bigfishcat.livingpictures.ui.popup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.ui.theme.Black
import com.github.bigfishcat.livingpictures.ui.theme.Blue
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme
import com.github.bigfishcat.livingpictures.ui.theme.PopupBackground
import com.github.bigfishcat.livingpictures.ui.theme.PopupStroke
import com.github.bigfishcat.livingpictures.ui.theme.Red
import com.github.bigfishcat.livingpictures.ui.theme.White

@Composable
fun PaletteColorPicker(
    action: (Intent) -> Unit = {}
) {
    Popup(
        alignment = Alignment.BottomCenter,
        offset = IntOffset(0, -108), // todo calculate bottom padding
        onDismissRequest = { action(Intent.HidePopup) }
    ) {
        PaletteColorPickerContent(action)
    }
}

@Composable
private fun PaletteColorPickerContent(action: (Intent) -> Unit) {
    Card(
        modifier = Modifier
            .background(PopupBackground),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, PopupStroke)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            IconButton(
                onClick = { action(Intent.ShowWheelColorPicker) }
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.palette),
                    contentDescription = stringResource(id = R.string.palette)
                )
            }

            listOf(White, Black, Red, Blue).forEach { color ->
                IconButton(
                    onClick = { action(Intent.SelectColor(color)) }
                ) {
                    Canvas(modifier = Modifier.size(32.dp)) {
                        drawCircle(
                            color = color,
                            radius = size.minDimension / 2.5f
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WheelColorPicker(

) {

}

@Preview
@Composable
fun DefaultPaletteColorPickerContent() {
    LivingPicturesTheme {
        PaletteColorPickerContent {}
    }
}