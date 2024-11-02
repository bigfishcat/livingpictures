package com.github.bigfishcat.livingpictures.ui.popup

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color as AndroidColor
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.core.graphics.toRect
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.ui.theme.Black
import com.github.bigfishcat.livingpictures.ui.theme.Blue
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme
import com.github.bigfishcat.livingpictures.ui.theme.Red
import com.github.bigfishcat.livingpictures.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun PaletteColorPicker(action: (Intent) -> Unit = {}) {
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
    PopupContentContainer {
        IconButton(
            onClick = { action(Intent.ShowWheelColorPicker) }
        ) {
            Image(
                painter = painterResource(id = R.drawable.palette),
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

@Composable
fun HueColorPicker(color: Color, action: (Intent) -> Unit = {}) {
    Popup(
        alignment = Alignment.BottomCenter,
        offset = IntOffset(0, -108), // todo calculate bottom padding
        onDismissRequest = { action(Intent.HidePopup) }
    ) {
        HueColorPickerContent(color, action)
    }
}

@Composable
fun HueColorPickerContent(color: Color, action: (Intent) -> Unit = {}) {
    PopupContentContainer {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val hsv = remember {
                val hsv = floatArrayOf(0f, 0f, 0f)
                AndroidColor.colorToHSV(color.toArgb(), hsv)

                mutableStateOf(
                    Triple(hsv[0], hsv[1], hsv[2])
                )
            }
            val selectedColor = remember(hsv.value) {
                mutableStateOf(Color.hsv(hsv.value.first, hsv.value.second, hsv.value.third))
            }

            HueBar { hue ->
                hsv.value = Triple(hue, hsv.value.second, hsv.value.third)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(selectedColor.value)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(id = R.string.confirm),
                color = Blue,
                modifier = Modifier.clickable {
                    action.invoke(Intent.SelectColor(selectedColor.value))
                }
            )
        }
    }
}

@Composable
fun HueBar(
    setColor: (Float) -> Unit
) {
    val scope = rememberCoroutineScope()
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val pressOffset = remember {
        mutableStateOf(Offset.Zero)
    }

    Canvas(
        modifier = Modifier
            .height(40.dp)
            .width(300.dp)
            .clip(RoundedCornerShape(50))
            .emitDragGesture(interactionSource)
    ) {
        val drawScopeSize = size
        val bitmap = Bitmap.createBitmap(size.width.toInt(), size.height.toInt(), Bitmap.Config.ARGB_8888)
        val hueCanvas = Canvas(bitmap)

        val huePanel = RectF(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())

        val hueColors = IntArray((huePanel.width()).toInt())
        var hue = 0f
        for (i in hueColors.indices) {
            hueColors[i] = AndroidColor.HSVToColor(floatArrayOf(hue, 1f, 1f))
            hue += 360f / hueColors.size
        }

        val linePaint = Paint()
        linePaint.strokeWidth = 0F
        for (i in hueColors.indices) {
            linePaint.color = hueColors[i]
            hueCanvas.drawLine(i.toFloat(), 0F, i.toFloat(), huePanel.bottom, linePaint)
        }

        drawBitmap(
            bitmap = bitmap,
            panel = huePanel
        )

        fun pointToHue(pointX: Float): Float {
            val width = huePanel.width()
            val x = when {
                pointX < huePanel.left -> 0F
                pointX > huePanel.right -> width
                else -> pointX - huePanel.left
            }
            return x * 360f / width
        }


        scope.collectForPress(interactionSource) { pressPosition ->
            val pressPos = pressPosition.x.coerceIn(0f..drawScopeSize.width)
            pressOffset.value = Offset(pressPos, 0f)
            val selectedHue = pointToHue(pressPos)
            setColor(selectedHue)
        }


        drawCircle(
            White,
            radius = size.height/2,
            center = Offset(pressOffset.value.x, size.height/2),
            style = Stroke(
                width = 2.dp.toPx()
            )
        )

    }
}

fun CoroutineScope.collectForPress(
    interactionSource: InteractionSource,
    setOffset: (Offset) -> Unit
) {
    launch {
        interactionSource.interactions.collect { interaction ->
            (interaction as? PressInteraction.Press)
                ?.pressPosition
                ?.let(setOffset)
        }
    }
}



private fun Modifier.emitDragGesture(
    interactionSource: MutableInteractionSource
): Modifier = composed {
    val scope = rememberCoroutineScope()

    pointerInput(Unit) {
        detectDragGestures { input, _ ->
            scope.launch {
                interactionSource.emit(PressInteraction.Press(input.position))
            }
        }
    }.clickable(interactionSource, null) {

    }
}

private fun DrawScope.drawBitmap(
    bitmap: Bitmap,
    panel: RectF
) {
    drawIntoCanvas {
        it.nativeCanvas.drawBitmap(
            bitmap,
            null,
            panel.toRect(),
            null
        )
    }
}

@Preview
@Composable
private fun DefaultPaletteColorPickerContent() {
    LivingPicturesTheme {
        PaletteColorPickerContent {}
    }
}

@Preview
@Composable
private fun DefaultHueBar() {
    LivingPicturesTheme {
        HueBar {}
    }
}