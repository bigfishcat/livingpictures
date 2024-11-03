package com.github.bigfishcat.livingpictures.model

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.random.Random

class LineProperties(
    val color: Color,
    val strokeWidth: Float = 4f
)

enum class PencilStrokeWidth(val value: Float) {
    Thin(4f),
    Medium(8f),
    Thick(12f)
}

enum class BrushStrokeWidth(val value: Float) {
    S1(15f),
    S2(25f),
    S3(35f),
    S4(45f),
    S5(55f)
}

sealed interface DrawObject {
    class Curve(val path: Path, val properties: LineProperties) : DrawObject

    class Erase(val path: Path, val strokeWidth: Float) : DrawObject

    class Circle(val center: Offset, val radius: Float, val properties: LineProperties) : DrawObject

    class Polygon(val path: Path, val properties: LineProperties) : DrawObject

    class Arrow(val start: Offset, val end: Offset, val properties: LineProperties) : DrawObject
}

data class PageUiState(
    val id: UUID = UUID.randomUUID(),
    val objects: List<DrawObject> = listOf(),
    val undoObjects: List<DrawObject> = listOf()
) {
    fun undo(): PageUiState {
        if (objects.isEmpty()) return this

        val undoObject = objects.last()
        return copy(
            objects = objects.dropLast(1),
            undoObjects = undoObjects + undoObject
        )
    }

    fun redo(): PageUiState {
        if (undoObjects.isEmpty()) return this

        val redoObject = undoObjects.last()
        return copy(
            objects = objects + redoObject,
            undoObjects = undoObjects.dropLast(1)
        )
    }

    fun addObject(
        paintProperties: PaintProperties,
        path: Path): PageUiState {
        val drawObject = when (paintProperties.instrument) {
            Instrument.Pencil -> DrawObject.Curve(path, LineProperties(paintProperties.color, paintProperties.pencilStrokeWidth.value))
            Instrument.Brush -> DrawObject.Curve(path, LineProperties(paintProperties.color, paintProperties.brushStrokeWidth.value))
            Instrument.Eraser -> DrawObject.Erase(path, paintProperties.eraserStrokeWidth.value)
            Instrument.Triangle, Instrument.Rectangle, Instrument.Circle, Instrument.Arrow -> {
                Log.e("DRAW_OBJECT", "Can't add draw object for instrument $paintProperties.instrument")
                return this
            }
        }

        return addObject(drawObject)
    }

    private fun addObject(drawObject: DrawObject): PageUiState {
        return copy(
            objects = objects + drawObject,
            undoObjects = listOf()
        )
    }
}

fun DrawScope.drawCurve(path: Path, color: Color, strokeWidth: Float) {
    drawPath(
        color = color,
        path = path,
        style = Stroke(
            width = strokeWidth,
            join = StrokeJoin.Round,
            cap = StrokeCap.Round
        )
    )
}

fun DrawScope.erase(path: Path, strokeWidth: Float) {
    drawPath(
        color = Color.Transparent,
        path = path,
        style = Stroke(
            width = strokeWidth,
            join = StrokeJoin.Round,
            cap = StrokeCap.Round
        ),
        blendMode = BlendMode.Clear
    )
}

fun DrawScope.draw(drawObject: DrawObject) {
    when (drawObject) {
        is DrawObject.Curve -> {
            drawCurve(
                drawObject.path,
                drawObject.properties.color,
                drawObject.properties.strokeWidth
            )
        }

        is DrawObject.Polygon -> {
            drawPath(
                color = drawObject.properties.color,
                path = drawObject.path,
                style = Stroke(
                    width = drawObject.properties.strokeWidth
                )
            )
        }

        is DrawObject.Circle -> {
            drawCircle(
                color = drawObject.properties.color,
                center = drawObject.center,
                radius = drawObject.radius,
                style = Stroke(
                    width = drawObject.properties.strokeWidth
                )
            )
        }

        is DrawObject.Arrow -> {
            drawLine(
                color = drawObject.properties.color,
                start = drawObject.start,
                end = drawObject.end,
                strokeWidth = drawObject.properties.strokeWidth
            )
            // TODO: draw arrow on the end of the line
        }

        is DrawObject.Erase -> {
            erase(drawObject.path, drawObject.strokeWidth)
        }
    }
}

suspend fun generateRandomObjects(canvasSize: Size): List<DrawObject> {
    return withContext(Dispatchers.Default) {
        val count = Random.nextInt(4) + 2
        (0 until count).map { generateRandomObject(canvasSize) }
    }
}

fun generateRandomObject(canvasSize: Size): DrawObject {
    return when (Random.nextInt(5)) {
        0, 1 -> generateRandomCircle(canvasSize)
        2, 3 -> generateRandomPolygon(canvasSize)
        else -> generateRandomArrow(canvasSize)
    }
}

private fun generateRandomPoint(canvasSize: Size) =
    Random.nextFloat() * canvasSize.width / 2 + canvasSize.width / 4 to
            Random.nextFloat() * canvasSize.height / 2 + canvasSize.height / 4

fun generateRandomCircle(canvasSize: Size): DrawObject.Circle {
    val (x, y) = generateRandomPoint(canvasSize)

    val maxRadius = (canvasSize.width - x)
        .coerceAtMost(x)
        .coerceAtMost(canvasSize.height - y)
        .coerceAtMost(y)

    return DrawObject.Circle(
        center = Offset(x, y),
        radius = Random.nextFloat() * maxRadius * 2 / 3 + maxRadius / 3,
        properties = LineProperties(randomColor(), Random.nextInt(16) + 8f)
    )
}

fun generateRandomPolygon(canvasSize: Size): DrawObject.Polygon {
    val heads = Random.nextInt(4) + 2

    val path = Path().apply {
        val points = (0 until heads).map { generateRandomPoint(canvasSize) }
        points.mapIndexed { i, (x, y) ->
            if (i == 0) {
                moveTo(x, y)
            } else {
                lineTo(x, y)
            }
        }
        val (x, y) = points[0]
        lineTo(x, y)
        close()
    }
    return DrawObject.Polygon(
        path = path,
        properties = LineProperties(randomColor(), Random.nextInt(16) + 8f)
    )
}

fun generateRandomArrow(canvasSize: Size): DrawObject.Arrow {
    return DrawObject.Arrow(
        start = Offset(
            Random.nextFloat() * canvasSize.width,
            Random.nextFloat() * canvasSize.height
        ),
        end = Offset(
            Random.nextFloat() * canvasSize.width,
            Random.nextFloat() * canvasSize.height
        ),
        properties = LineProperties(randomColor(), Random.nextInt(12) + 4f)
    )
}

fun randomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        blue = Random.nextFloat(),
        green = Random.nextFloat()
    )
}