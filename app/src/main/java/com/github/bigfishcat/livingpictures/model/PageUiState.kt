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

sealed interface DrawObject {
    class Curve(val path: Path, val properties: LineProperties) : DrawObject

    class Erase(val path: Path, val strokeWidth: Float = 10f) : DrawObject

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

    fun addObject(instrument: Instrument, color: Color, path: Path): PageUiState {
        val drawObject = when (instrument) {
            Instrument.Pencil -> DrawObject.Curve(path, LineProperties(color))
            Instrument.Brush -> DrawObject.Curve(path, LineProperties(color, 10f))
            Instrument.Eraser -> DrawObject.Erase(path)
            Instrument.Triangle, Instrument.Rectangle, Instrument.Circle, Instrument.Arrow -> {
                Log.e("DRAW_OBJECT", "Can't add draw object for instrument $instrument")
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

fun DrawScope.erase(path: Path, strokeWidth: Float = 10f) {
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
        val count = Random.nextInt(5)
        (0 until count).map { generateRandomObject(canvasSize) }
    }
}

fun generateRandomObject(canvasSize: Size): DrawObject {
    return when (Random.nextInt(3)) {
        0 -> generateRandomCircle(canvasSize)
        1 -> generateRandomPolygon(canvasSize)
        else -> generateRandomArrow(canvasSize)
    }
}

fun generateRandomCircle(canvasSize: Size): DrawObject.Circle {
    val x = Random.nextFloat() * canvasSize.width
    val y = Random.nextFloat() * canvasSize.height

    val maxRadius = (canvasSize.width - x)
        .coerceAtMost(x)
        .coerceAtMost(canvasSize.height - y)
        .coerceAtMost(y)

    return DrawObject.Circle(
        center = Offset(x, y),
        radius = Random.nextFloat() * maxRadius,
        properties = LineProperties(randomColor(), 8f)
    )
}

fun generateRandomPolygon(canvasSize: Size): DrawObject.Polygon {
    val heads = Random.nextInt(4) + 2

    val path = Path().apply {
        val points = (0 until heads).map {
            Random.nextFloat() * canvasSize.width to Random.nextFloat() * canvasSize.height
        }
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
    return DrawObject.Polygon(path = path, properties = LineProperties(randomColor(), 8f))
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
        properties = LineProperties(randomColor())
    )
}

fun randomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        blue = Random.nextFloat(),
        green = Random.nextFloat()
    )
}