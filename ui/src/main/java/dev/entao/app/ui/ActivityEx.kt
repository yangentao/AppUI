@file:Suppress("unused")

package dev.entao.app.ui

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.*


fun Activity.bitmapOfContent(callBack: (Bitmap?) -> Unit) {
    this.bitmapOfView(this.getContentView(), callBack)
}

@Suppress("DEPRECATION")
fun Activity.bitmapOfView(view: View, callBack: (Bitmap?) -> Unit) {
    val window: Window = this.window
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val location = IntArray(2)
        view.getLocationInWindow(location)
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888, true)
        PixelCopy.request(
            window, Rect(location[0], location[1], location[0] + view.width, location[1] + view.height), bitmap, { result ->
                if (result == PixelCopy.SUCCESS) {
                    callBack(bitmap)
                } else {
                    callBack(null)
                }
            }, Handler(Looper.getMainLooper())
        )
    } else {
        var bitmap: Bitmap? = null
        view.isDrawingCacheEnabled = true
        view.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        val cache: Bitmap? = view.getDrawingCache(false)
        if (cache != null && !cache.isRecycled) {
            bitmap = Bitmap.createBitmap(cache)
        }
        view.destroyDrawingCache()
        view.setDrawingCacheEnabled(false)
        callBack(bitmap)
    }

}


fun Activity.getContentView(): View {
    return this.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
}

@Suppress("DEPRECATION")
fun Activity.fullScreen(full: Boolean) {
    if (full) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    } else {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars())
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
    }
}
