package com.github.bigfishcat.livingpictures.ui.popup

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.github.bigfishcat.livingpictures.model.AppUiState
import com.github.bigfishcat.livingpictures.model.BrushStrokeWidth
import com.github.bigfishcat.livingpictures.model.Intent
import com.github.bigfishcat.livingpictures.model.PencilStrokeWidth
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme

@Composable
fun PencilStrokeWidthPicker(
    bottomBarHeight: Int,
    appState: AppUiState,
    action: (Intent) -> Unit = {}
) {
    Popup(
        alignment = Alignment.BottomCenter,
        offset = IntOffset(0, -bottomBarHeight),
        onDismissRequest = { action(Intent.HidePopup) }
    ) {
        PencilStrokeWidthPickerContent(appState, action)
    }
}

@Composable
private fun PencilStrokeWidthPickerContent(
    appState: AppUiState,
    action: (Intent) -> Unit = {}
) {
    PopupContentContainer {
        Column {
            PencilStrokeWidth.entries.forEach { strokeWidth ->
                Canvas(
                    modifier = Modifier
                        .width(196.dp)
                        .height(24.dp)
                        .padding(horizontal = 6.dp)
                        .clickable { action.invoke(Intent.SelectPencilStrokeWidth(strokeWidth)) }
                ) {
                    drawLine(
                        color = appState.paintProperties.color,
                        start = Offset(3f, size.height / 2),
                        end = Offset(size.width - 3, size.height / 2),
                        strokeWidth = strokeWidth.value
                    )

                    if (strokeWidth == appState.paintProperties.pencilStrokeWidth) {
                        val height = strokeWidth.value + 6f
                        drawRect(
                            color = appState.paintProperties.color,
                            topLeft = Offset(0f, size.height / 2 - height / 2),
                            size = Size(size.width, height),
                            style = Stroke(width = 2f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun BrushStrokeWidthPicker(
    bottomBarHeight: Int,
    appState: AppUiState,
    action: (Intent) -> Unit = {}
) {
    Popup(
        alignment = Alignment.BottomCenter,
        offset = IntOffset(0, -bottomBarHeight),
        onDismissRequest = { action(Intent.HidePopup) }
    ) {
        BrushStrokeWidthPickerContent(
            appState.paintProperties.color,
            appState.paintProperties.brushStrokeWidth
        ) { action.invoke(Intent.SelectBrushStrokeWidth(it)) }
    }
}

@Composable
fun EraserStrokeWidthPicker(
    bottomBarHeight: Int,
    appState: AppUiState,
    action: (Intent) -> Unit = {}
) {
    Popup(
        alignment = Alignment.BottomCenter,
        offset = IntOffset(0, -bottomBarHeight),
        onDismissRequest = { action(Intent.HidePopup) }
    ) {
        BrushStrokeWidthPickerContent(
            appState.paintProperties.color,
            appState.paintProperties.eraserStrokeWidth
        ) { action.invoke(Intent.SelectEraserStrokeWidth(it)) }
    }
}

@Composable
private fun BrushStrokeWidthPickerContent(
    color: Color,
    initialStrokeWidth: BrushStrokeWidth,
    selectStrokeWidth: (BrushStrokeWidth) -> Unit = {}
) {
    PopupContentContainer {
        BrushStrokeWidth.entries.forEach { strokeWidth ->
            Canvas(
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
                    .padding(6.dp)
                    .clickable { selectStrokeWidth.invoke(strokeWidth) }
            ) {
                drawCircle(
                    color = color,
                    center = Offset(size.height / 2, size.height / 2),
                    radius = strokeWidth.value
                )

                if (strokeWidth == initialStrokeWidth) {
                    drawCircle(
                        color = color,
                        center = Offset(size.height / 2, size.height / 2),
                        radius = strokeWidth.value + 4,
                        style = Stroke(width = 2f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
private fun DefaultPencilStrokeWidthPickerContent() {
    LivingPicturesTheme {
        PencilStrokeWidthPickerContent(AppUiState()) {}
    }
}

@Preview
@Composable
private fun DefaultBrushStrokeWidthPickerContent() {
    LivingPicturesTheme {
        BrushStrokeWidthPickerContent(Color.Blue, BrushStrokeWidth.S3) {}
    }
}