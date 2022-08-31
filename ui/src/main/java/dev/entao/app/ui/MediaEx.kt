@file:Suppress("unused", "MemberVisibilityCanBePrivate", "PropertyName")

package dev.entao.app.ui

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import dev.entao.app.basic.closeAuto
import java.io.File
import java.io.IOException


class MediaInfo(private val context: Context, val uri: Uri) {
    var displayName: String? = null
    var mimeType: String? = null
    var size = 0
    var width = 0
    var height = 0
    var OK = false


    init {
        ////content://media/external/images/media/10025
        if (uri.host == "media" && uri.scheme == "content") {
            context.contentResolver.query(
                uri, arrayOf(
                    MediaStore.MediaColumns.DISPLAY_NAME,
                    MediaStore.MediaColumns.MIME_TYPE,
                    MediaStore.MediaColumns.SIZE,
                    MediaStore.MediaColumns.WIDTH,
                    MediaStore.MediaColumns.HEIGHT
                ), null, null, "_id ASC LIMIT 1"
            )?.use {
                if (it.moveToNext()) {
                    displayName = it.getString(0)
                    mimeType = it.getString(1)
                    size = it.getInt(2)
                    width = it.getInt(3)
                    height = it.getInt(4)
                    OK = true
                }
            }

        }
    }
}


object Medias {
    fun videoMillSeconds(file: File): Long {
        val m = MediaMetadataRetriever()
        m.setDataSource(file.absolutePath)
        m.closeAuto {
            val ms = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: return 0L
            return ms.toLongOrNull() ?: 0L
        }
    }


    fun videoThumbnail(context: Context, uri: Uri): Bitmap? {
        val retriver = MediaMetadataRetriever()
        retriver.setDataSource(context, uri)
        val bmp = retriver.frameAtTime
        retriver.release()
        return bmp
    }

    fun playSound(file: File, callback: (MediaPlayer) -> Unit): MediaPlayer? {
        if (!file.exists()) {
            return null
        }
        val player = MediaPlayer()
        player.setOnCompletionListener { mp ->
            mp.release()
        }
        player.setOnPreparedListener(callback)
        try {
            player.setDataSource(file.absolutePath)
            player.prepare()
            player.start()
            return player
        } catch (e: IOException) {
            e.printStackTrace()
            player.release()
        }
        return null
    }
}