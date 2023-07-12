package com.darkndev.everpics.features

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.darkndev.everpics.R
import com.darkndev.everpics.utils.sdkVersion29AndAbove

class ImageDownloader(
    private val context: Context
) : Downloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long {
        val name =
            "${context.getString(R.string.app_name)}_fromUnsplash_${System.currentTimeMillis()}.jpg"
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("image/jpeg")
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(name)

        sdkVersion29AndAbove {
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                name
            )
        } ?: request.setDestinationInExternalFilesDir(
            context,
            Environment.DIRECTORY_DOWNLOADS,
            name
        )

        return downloadManager.enqueue(request)
    }
}