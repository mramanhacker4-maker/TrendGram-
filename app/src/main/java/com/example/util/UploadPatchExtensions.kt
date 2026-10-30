package com.example.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File

/**
 * Upload Patch Extensions for Video and Media Handling in TrendGram
 */
data class VideoMetadataPatch(
    val fileName: String,
    val sizeBytes: Long,
    val mimeType: String,
    val isMp4OrSupported: Boolean
)

fun Uri.getMetadataPatch(context: Context): VideoMetadataPatch {
    var name = "video_${System.currentTimeMillis()}.mp4"
    var size = 0L
    val contentResolver: ContentResolver = context.contentResolver
    val mime = contentResolver.getType(this) ?: "video/mp4"

    if (this.scheme == ContentResolver.SCHEME_CONTENT) {
        contentResolver.query(this, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            if (cursor.moveToFirst()) {
                if (nameIndex != -1) name = cursor.getString(nameIndex) ?: name
                if (sizeIndex != -1) size = cursor.getLong(sizeIndex)
            }
        }
    } else if (this.scheme == "file") {
        this.path?.let { path ->
            val file = File(path)
            if (file.exists()) {
                name = file.name
                size = file.length()
            }
        }
    }

    val isSupported = mime.startsWith("video/") || name.endsWith(".mp4", ignoreCase = true) || name.endsWith(".mov", ignoreCase = true)

    return VideoMetadataPatch(
        fileName = name,
        sizeBytes = size,
        mimeType = mime,
        isMp4OrSupported = isSupported
    )
}

fun Long.formatFileSize(): String {
    return when {
        this >= 1024 * 1024 * 1024 -> String.format("%.2f GB", this / (1024.0 * 1024 * 1024))
        this >= 1024 * 1024 -> String.format("%.1f MB", this / (1024.0 * 1024))
        this >= 1024 -> String.format("%.0f KB", this / 1024.0)
        else -> "$this B"
    }
}
