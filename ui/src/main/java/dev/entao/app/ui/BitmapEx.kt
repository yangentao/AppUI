package dev.entao.app.ui

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.Px
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import dev.entao.app.basic.useSafe
import java.io.File
import kotlin.math.min

fun Bitmap.toDrawable(context: Context): BitmapDrawable = BitmapDrawable(context.resources, this)

fun Bitmap.tint(context: Context, color: Int): Bitmap {
    val w = this.getScaledWidth(context.resources.displayMetrics)
    val h = this.getScaledHeight(context.resources.displayMetrics)
    val target = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(target)
    val paint = Paint()
    paint.color = color
    val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())
    canvas.drawRect(rect, paint)
    paint.isAntiAlias = true
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    val rect2 = Rect(0, 0, w, h)
    canvas.drawBitmap(this, rect2, rect2, paint)
    return target
}


fun Bitmap.maxEdge(@Px maxEdge: Int): Bitmap {
    return this.limit(maxEdge, maxEdge)
}


//限制最大的高宽, 等比例缩放, 比如, 原图 300 * 400, limi(200,200)将会将图片变为原来的1/2, 150* 200
fun Bitmap.limit(@Px maxWidth: Int, @Px maxHeight: Int = maxWidth): Bitmap {
    if (maxWidth <= 0 || maxHeight <= 0) {
        return this
    }
    if (width < maxWidth && height < maxHeight) {
        return this
    }
    val fw = if (width > maxWidth) {
        maxWidth * 1.0 / width
    } else 1.0
    val fh = if (height > maxHeight) {
        maxHeight * 1.0 / height
    } else 1.0
    val f = min(fw, fh).toFloat()
    return scale(f, f)
}

fun Bitmap.scaleTo(@Px newWidth: Int, @Px newHeight: Int): Bitmap {
    val scaleWidth = newWidth.toFloat() / width
    val scaleHeight = newHeight.toFloat() / height
    return scale(scaleWidth, scaleHeight)
}


fun Bitmap.scale(fWidth: Float, fHeight: Float): Bitmap {
    val matrix = Matrix()
    matrix.postScale(fWidth, fHeight)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}

fun Bitmap.saveJpg(saveTo: File): Boolean {
    return saveTo.outputStream().useSafe {
        this.compress(Bitmap.CompressFormat.JPEG, 100, it)
    } ?: false
}

fun Bitmap.circled(context: Context): Drawable = RoundedBitmapDrawableFactory.create(context.resources, this).apply {
    isCircular = true
}


fun Bitmap.rounded(context: Context): Drawable {
    return this.rounded(context, (context.resources.displayMetrics.density * 8).toInt())
}

fun Bitmap.rounded(context: Context, corner: Int): Drawable {
    val d = RoundedBitmapDrawableFactory.create(context.resources, this)
    d.cornerRadius = corner.toFloat()
    return d
}

fun Bitmap.rotate(degree: Int): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(degree.toFloat())
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}

//圆,  高和宽较最小的值做直径,
fun Bitmap.oval(context: Context): Bitmap {
    val w = this.getScaledWidth(context.resources.displayMetrics)
    val h = this.getScaledHeight(context.resources.displayMetrics)
    val paint = Paint()
    paint.isAntiAlias = true

    val d = min(w, h)
    val corner = d / 2
    val target = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(target)
    val rect = RectF(0f, 0f, d.toFloat(), d.toFloat())
    canvas.drawRoundRect(rect, corner.toFloat(), corner.toFloat(), paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, (-(w - d) / 2).toFloat(), (-(h - d) / 2).toFloat(), paint)
    return target
}

//高宽的一半做圆角, 保持高宽比
fun Bitmap.roundBmp(context: Context): Bitmap {
    val w = this.getScaledWidth(context.resources.displayMetrics)
    val h = this.getScaledHeight(context.resources.displayMetrics)
    val paint = Paint()
    paint.isAntiAlias = true
    val target = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(target)
    val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())
    canvas.drawRoundRect(rect, (w / 2).toFloat(), (h / 2).toFloat(), paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, 0f, 0f, paint)
    return target
}

// 高和宽较最小的值做直径,  圆角正方形
fun Bitmap.roundSqure(context: Context, @Px corner: Float): Bitmap {
    val w = this.getScaledWidth(context.resources.displayMetrics)
    val h = this.getScaledHeight(context.resources.displayMetrics)
    val paint = Paint()
    paint.isAntiAlias = true

    val d = min(w, h)
    val target = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(target)
    val rect = RectF(0f, 0f, d.toFloat(), d.toFloat())
    canvas.drawRoundRect(rect, corner, corner, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, (-(w - d) / 2).toFloat(), (-(h - d) / 2).toFloat(), paint)
    return target
}

//圆角bitmap, 保持高宽比
fun Bitmap.roundBmp(context: Context, @Px corner: Float): Bitmap {
    val w = this.getScaledWidth(context.resources.displayMetrics)
    val h = this.getScaledHeight(context.resources.displayMetrics)
    val paint = Paint()
    paint.isAntiAlias = true
    val target = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(target)

    val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())
    canvas.drawRoundRect(rect, corner, corner, paint)
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, 0f, 0f, paint)
    return target
}