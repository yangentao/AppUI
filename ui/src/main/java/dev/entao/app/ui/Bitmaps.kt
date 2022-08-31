package dev.entao.app.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.DisplayMetrics
import android.util.Size
import androidx.annotation.Px
import java.io.InputStream
import kotlin.math.max

typealias BitmapFactoryOptions = BitmapFactory.Options
typealias BitmapConfig = Bitmap.Config

val Size.maxEdge: Int get() = max(this.width, this.height)

fun InputStream.bitmapSize(): Size = Bitmaps.sizeByStream(this)
fun InputStream.bitmap(config: BitmapConfig?, sample: Int = 1): Bitmap? = Bitmaps.fromStream(this, config, sample)

object Bitmaps {

    fun line(@Px width: Int, @Px height: Int, color: Int): Bitmap {
        val target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        target.density = DisplayMetrics.DENSITY_HIGH
        val canvas = Canvas(target)
        canvas.drawColor(color)
        return target
    }

    fun sizeByStream(ins: InputStream): Size {
        val opt = ins.use {
            BitmapFactoryOptions().apply {
                inJustDecodeBounds = true
                BitmapFactory.decodeStream(ins, null, this)
            }
        }
        return Size(opt.outWidth, opt.outHeight)
    }

    fun fromStream(ins: InputStream, config: BitmapConfig?, sample: Int = 1): Bitmap? {
        val opt = BitmapFactoryOptions().apply {
            inSampleSize = max(1, sample)
            inPreferredConfig = config
        }
        return BitmapFactory.decodeStream(ins, null, opt)
    }


    fun fromStream(maxEdge: Int, config: BitmapConfig?, streamProvidor: () -> InputStream?): Bitmap? {
        val size = streamProvidor()?.bitmapSize() ?: return null
        return streamProvidor()?.bitmap(config, max(1, size.maxEdge / maxEdge))
    }

}