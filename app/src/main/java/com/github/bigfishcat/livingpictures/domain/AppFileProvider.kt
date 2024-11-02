package com.github.bigfishcat.livingpictures.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.content.FileProvider.getUriForFile
import com.github.bigfishcat.livingpictures.R
import java.io.File


class AppFileProvider : FileProvider()

fun Context.shareFile(file: File, mimeType: String = "image/gif") {
    val contentUri: Uri = getUriForFile(this, "com.github.bigfishcat", file)
    val intent = ShareCompat.IntentBuilder(this)
        .setType(mimeType)
        .setStream(contentUri)
        .setChooserTitle(R.string.share_title)
        .createChooserIntent()
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    try {
        startActivity(intent)
    } catch (e: Exception) {
        Log.e("SHARE", "Failed to share gif file", e)
        Toast.makeText(this, R.string.share_failed, Toast.LENGTH_LONG).show()
    }
}