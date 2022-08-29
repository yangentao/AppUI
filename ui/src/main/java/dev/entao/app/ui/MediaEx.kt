@file:Suppress("unused", "MemberVisibilityCanBePrivate", "PropertyName")

package dev.entao.app.ui

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import dev.entao.app.basic.closeAuto
import dev.entao.app.sql.RowData
import dev.entao.app.sql.UriQuery
import dev.entao.app.sql.firstDataClose
import java.io.File
import java.io.IOException


class MediaInfo(private val context: Context, val uri: Uri) {
    var displayName: String? = null
    var mimeType: String? = null
    var size = 0
    var width = 0
    var height = 0
    var record: RowData? = null
    var OK = false


    init {
        ////content://media/external/images/media/10025
        if (uri.host == "media" && uri.scheme == "content") {
            UriQuery(context, uri).limit(1).query()?.firstDataClose?.also { map ->
                displayName = map.str(MediaStore.MediaColumns.DISPLAY_NAME)
                mimeType = map.str(MediaStore.MediaColumns.MIME_TYPE)
                size = map.int(MediaStore.MediaColumns.SIZE) ?: 0
                width = map.int(MediaStore.MediaColumns.WIDTH) ?: 0
                height = map.int(MediaStore.MediaColumns.HEIGHT) ?: 0
                record = map
                OK = true
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