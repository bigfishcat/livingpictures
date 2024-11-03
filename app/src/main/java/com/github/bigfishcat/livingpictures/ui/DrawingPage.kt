package com.github.bigfishcat.livingpictures.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitTouchSlopOrCancellation
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.bigfishcat.livingpictures.R
import com.github.bigfishcat.livingpictures.model.Instrument
import com.github.bigfishcat.livingpictures.model.MotionType
import com.github.bigfishcat.livingpictures.model.PageUiState
import com.github.bigfishcat.livingpictures.model.PaintProperties
import com.github.bigfishcat.livingpictures.model.draw
import com.github.bigfishcat.livingpictures.model.drawCurve
import com.github.bigfishcat.livingpictures.model.erase
import com.github.bigfishcat.livingpictures.ui.theme.LivingPicturesTheme

@Composable
fun DrawingPage(
    modifier: Modifier = Modifier,
    pageUiState: PageUiState,
    paintProperties: PaintProperties,
    enabled: Boolean,
    updatePageState: (PageUiState) -> Unit = {}
) {
    val selectedInstrument = paintProperties.instrument
    val color = paintProperties.color

    var motionType by remember {
        mutableStateOf(MotionType.Idle)
    }

    var currentPosition by remember {
        mutableStateOf(Offset.Unspecified)
    }

    var previousPosition by remember {
        mutableStateOf(Offset.Unspecified)
    }

    var currentPath by remember {
        mutableStateOf(Path())
    }

    fun onStart(pointer: PointerInputChange) {
        motionType = MotionType.Down
        currentPosition = pointer.position
        if (pointer.pressed != pointer.previousPressed) pointer.consume()
    }

    fun onMove(pointer: PointerInputChange) {
        motionType = MotionType.Move
        currentPosition = pointer.position
        // TODO: handle move for figures
        if (pointer.positionChange() != Offset.Zero) pointer.consume()
    }

    fun onEnd(pointer: PointerInputChange) {
        motionType = MotionType.Up
        if (pointer.pressed != pointer.previousPressed) pointer.consume()
    }

    fun handleMotion() {
        when (motionType) {
            MotionType.Down -> {
                if (selectedInstrument.canDraw) {
                    currentPath.moveTo(currentPosition.x, currentPosition.y)
                }

                previousPosition = currentPosition
            }

            MotionType.Move -> {
                if (selectedInstrument.canDraw) {
                    currentPath.quadraticTo(
                        previousPosition.x,
                        previousPosition.y,
                        (previousPosition.x + currentPosition.x) / 2,
                        (previousPosition.y + currentPosition.y) / 2
                    )
                }

                previousPosition = currentPosition
            }

            MotionType.Up -> {
                if (selectedInstrument.canDraw) {
                    currentPath.lineTo(currentPosition.x, currentPosition.y)
                    updatePageState.invoke(
                        pageUiState.addObject(paintProperties, currentPath)
                    )
                    currentPath = Path()
                }

                currentPosition = Offset.Unspecified
                previousPosition = currentPosition
                motionType = MotionType.Idle
            }

            else -> Unit
        }
    }

    Box(modifier = modifier.padding(6.dp)) {
        Image(
            painter = painterResource(id = R.drawable.background),
            modifier = Modifier.fillMaxSize(),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Canvas(modifier = Modifier
            .fillMaxSize()
            .clipToBounds()
            .pointerInput(Unit) {
                awaitEachGesture {
                    handleMotionEvents(::onStart, ::onMove, ::onEnd)
                }
            }
        ) {
            if (enabled) handleMotion()

            with(drawContext.canvas.nativeCanvas) {
                val checkPoint = saveLayer(null, null)
                pageUiState.objects.forEach { draw(it) }

                if (motionType != MotionType.Idle && !currentPath.isEmpty && enabled) {
                    when (selectedInstrument) {
                        Instrument.Eraser -> erase(currentPath, paintProperties.eraserStrokeWidth.value)
                        Instrument.Pencil -> drawCurve(currentPath, color, paintProperties.pencilStrokeWidth.value)
                        Instrument.Brush -> drawCurve(currentPath, color, paintProperties.brushStrokeWidth.value)
                        else -> {}
                    }
                }
                restoreToCount(checkPoint)
            }
        }
    }
}

private suspend fun AwaitPointerEventScope.handleMotionEvents(
    onStart: (PointerInputChange) -> Unit,
    onMove: (PointerInputChange) -> Unit,
    onEnd: (PointerInputChange) -> Unit
) {
    val down: PointerInputChange = awaitFirstDown()
    onStart(down)

    val change = awaitTouchSlopOrCancellation(down.id) { change, _ ->
        if (change.positionChange() != Offset.Zero) change.consume()
    }

    if (change != null) {
        var pointer = down
        drag(change.id) { pointerInputChange ->
            pointer = pointerInputChange
            onMove(pointer)
        }

        onEnd(pointer)
    } else {
        onEnd(down)
    }
}

@Preview
@Composable
private fun DefaultDrawingPage() {
    LivingPicturesTheme {
        DrawingPage(
            pageUiState = PageUiState(),
            paintProperties = PaintProperties(),
            enabled = false
        )
    }
}