package com.github.bigfishcat.livingpictures.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import java.util.UUID

class LineProperties(
    val color: Color,
    val strokeWidth: Float = 10f
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
}