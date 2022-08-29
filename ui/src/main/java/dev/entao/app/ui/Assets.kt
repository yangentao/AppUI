@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package dev.entao.app.ui

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.DisplayMetrics
import dev.entao.app.basic.readText
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipInputStream


class Assets(val context: Context) {
    private val manager: AssetManager get() = context.assets

    fun bufferedStream(name: String): InputStream {
        return manager.open(name, AssetManager.ACCESS_BUFFER)
    }

    fun stream(name: String): InputStream {
        return manager.open(name, AssetManager.ACCESS_STREAMING)
    }

    fun streamZip(name: String): ZipInputStream {
        val inStream = stream(name)
        val bis = BufferedInputStream(inStream, 32 * 1024)
        return ZipInputStream(bis)
    }

    fun uri(filename: String): Uri {
        return Uri.parse("file:///android_asset/$filename")
    }

    fun list(path: String): Array<String> {
        var path2 = path
        try {
            if (path2.endsWith("/")) {
                path2 = path2.substring(0, path2.length - 1)
            }
            return manager.list(path2) as Array<String>
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return arrayOf()
    }

    fun text(path: String): String? {
        try {
            bufferedStream(path).use {
                return it.readText()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    // 读取图片 , 适合小图片!! 设置密度是hdpi, 不支持9png!!!!
    fun bitmap(path: String): Bitmap? {
        try {
            bufferedStream(path).use { ins ->
                return BitmapFactory.decodeStream(ins)?.also { it.density = DisplayMetrics.DENSITY_HIGH }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun drawable(path: String): Drawable? {
        try {
            bufferedStream(path).use { bs ->
                return Drawable.createFromResourceStream(context.resources, null, bs, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


}