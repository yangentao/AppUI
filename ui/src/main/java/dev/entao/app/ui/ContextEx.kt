package dev.entao.app.ui

import android.annotation.TargetApi
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.view.Gravity
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

val Context.density: Float get() = this.resources.displayMetrics.density

fun Context.dp(value: Int): Int = (this.density * value).toInt()
fun Context.dp(value: Float): Float = this.density * value
fun Context.dp(value: Double): Double = this.density * value

fun Context.resDrawable(resId: Int): Drawable = ResourcesCompat.getDrawable(this.resources, resId, this.theme) ?: error("NO drawable: $resId")
fun Context.resColor(resId: Int): Int = ResourcesCompat.getColor(this.resources, resId, this.theme)


fun Context.getAppIcon(packageName: String? = null): Bitmap? {
    val pm: PackageManager = this.packageManager
    val pkg: String = packageName ?: this.packageName
    if (Build.VERSION.SDK_INT < 26) {
        try {
            val d = pm.getApplicationIcon(pkg)
            if (d is BitmapDrawable) {
                return d.bitmap
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    } else {
        return getAppIcon2(pm, pkg)
    }

}

@TargetApi(Build.VERSION_CODES.O)
private fun getAppIcon2(pm: PackageManager, pkg: String): Bitmap? {
    try {
        val drawable = pm.getApplicationIcon(pkg)
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        if (drawable is AdaptiveIconDrawable) {
            val layerDrawable = LayerDrawable(arrayOf(drawable.background, drawable.foreground))
            val width = layerDrawable.intrinsicWidth
            val height = layerDrawable.intrinsicHeight
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            layerDrawable.setBounds(0, 0, canvas.width, canvas.height)
            layerDrawable.draw(canvas)
            return bitmap
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return null
}


fun Context.copyToClipboard(text: String) {
    val m: ClipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val data = ClipData.newPlainText("", text)
    m.setPrimaryClip(data)
}


fun Context.hasPermissions(ps: Set<String>): Boolean {
    for (p in ps) {
        if (!hasPermission(p)) {
            return false
        }
    }
    return true
}

fun Context.hasPermission(p: String): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, p)
    } else {
        true
    }
}

fun Context.toastShort(vararg texts: String): Toast {
    val t = Toast.makeText(this, texts.joinToString(""), Toast.LENGTH_SHORT)
    t.setGravity(Gravity.CENTER, 0, 0)
    t.show()
    return t
}

fun Context.toastLong(vararg texts: String): Toast {
    val t = Toast.makeText(this, texts.joinToString(""), Toast.LENGTH_LONG)
    t.setGravity(Gravity.CENTER, 0, 0)
    t.show()
    return t
}