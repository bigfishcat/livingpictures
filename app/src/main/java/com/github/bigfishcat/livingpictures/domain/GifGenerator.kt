package com.github.bigfishcat.livingpictures.domain

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.github.bigfishcat.livingpictures.model.PageUiState
import com.mlapadula.gifencoder.GifEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class GifGenerator(context: Context) {
    private val cacheFolder by lazy {
        context.externalCacheDir ?: context.cacheDir
    }

    suspend fun saveToFile(
        pages: List<PageUiState>,
        delayMs: Int,
        bitmapFactory: suspend (PageUiState) -> Bitmap?
    ): File? {
        return withContext(Dispatchers.IO) {
            try {
                val gifEncoder = GifEncoder()
                val gifFile = File(cacheFolder, "${UUID.randomUUID()}.gif")

                gifEncoder.start(gifFile.canonicalPath)
                gifEncoder.setDelay(delayMs)

                pages.forEach { page ->
                    bitmapFactory.invoke(page)?.let {
                        gifEncoder.addFrame(it)
                    }
                }

                gifEncoder.finish()
                gifFile
            } catch (e: Exception) {
                Log.e("EXPORT", "Failed to export images to gif", e)
                null
            }
        }
    }
}