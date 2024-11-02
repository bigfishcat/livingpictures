package com.github.bigfishcat.livingpictures.model

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import java.util.UUID

class LineProperties(
    val color: Color,
    val strokeWidth: Float = 4f
)

sealed interface DrawObject {
    class Curve(val path: Path, val properties: LineProperties) : DrawObject

    class Erase(val path: Path, val strokeWidth: Float = 10f) : DrawObject

    class Circle(val center: Offset, val radius: Float, val properties: LineProperties) : DrawObject

    class Polygon(val path: Path, val properties: LineProperties) : DrawObject

    class Arrow(val start: Offset, val end: Offset, val properties: LineProperties): DrawObject
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
        val drawObject = when(instrument) {
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

        is DrawObject.Arrow -> TODO()

        is DrawObject.Erase -> {
            erase(drawObject.path, drawObject.strokeWidth)
        }
    }
}