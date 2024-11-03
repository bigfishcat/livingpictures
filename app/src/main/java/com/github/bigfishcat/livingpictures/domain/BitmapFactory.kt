package com.github.bigfishcat.livingpictures.domain

import android.graphics.Bitmap
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.github.bigfishcat.livingpictures.model.PageUiState
import com.github.bigfishcat.livingpictures.model.draw
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BitmapFactory {
    suspend fun drawToBitmap(
        page: PageUiState,
        background: ImageBitmap,
        size: Size
    ): ImageBitmap {
        val drawScope = CanvasDrawScope()
        val bitmap = withContext(Dispatchers.Default) {
            Bitmap.createScaledBitmap(
                background.asAndroidBitmap(),
                size.width.toInt(),
                size.height.toInt(),
                false
            ).asImageBitmap()
        }
        val canvas = Canvas(bitmap)

        with(canvas.nativeCanvas) {
            val checkPoint = saveLayer(null, null)
            drawScope.draw(
                density = Density(1f),
                layoutDirection = LayoutDirection.Ltr,
                canvas = canvas,
                size = size,
            ) {
                page.objects.forEach { draw(it) }
            }
            restoreToCount(checkPoint)
        }
        return bitmap
    }
}