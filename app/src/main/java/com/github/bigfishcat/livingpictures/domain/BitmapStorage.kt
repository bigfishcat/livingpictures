package com.github.bigfishcat.livingpictures.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID

class BitmapStorage(context: Context) {
    private val sessionId = UUID.randomUUID().toString()
    private val cacheFolder by lazy {
        context.externalCacheDir ?: context.cacheDir
    }
    private val sessionFolder by lazy {
        File(cacheFolder, sessionId).also {
            it.mkdirs()
        }
    }

    suspend fun saveBitmap(pageId: UUID, bitmap: Bitmap) {
        withContext(Dispatchers.IO) {
            try {
                FileOutputStream(File(sessionFolder, pageId.toString())).use { out ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
            } catch (e: IOException) {
                Log.e("BITMAP", "Failed to save bitmap for page $pageId", e)
            }
        }
    }

    suspend fun readBitmap(pageId: UUID): Bitmap? {
        return withContext(Dispatchers.IO) {
            try {
                val imageFile = File(sessionFolder, pageId.toString())
                if (imageFile.exists()) {
                    BitmapFactory.decodeFile(imageFile.path)
                } else {
                    null
                }
            } catch (e: IOException) {
                Log.e("BITMAP", "Failed to read bitmap for page $pageId", e)
                null
            }
        }
    }

    suspend fun deleteBitmap(pageId: UUID) {
        return withContext(Dispatchers.IO) {
            val imageFile = File(sessionFolder, pageId.toString())
            if (imageFile.exists()) {
                imageFile.delete()
            }
        }
    }
}