package dev.entao.app.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.DrawableCompat
import dev.entao.app.basic.PX


val Drawable.tintedWhite: Drawable
    get() {
        return this.tinted(Color.WHITE)
    }


fun Drawable.tinted(color: Int): Drawable {
    val dc = DrawableCompat.wrap(this.mutate())
    DrawableCompat.setTint(dc, color)
    return dc
}

fun Drawable.tinted(colorList: ColorStateList): Drawable {
    val dc = DrawableCompat.wrap(this.mutate())
    DrawableCompat.setTintList(dc, colorList)
    return dc
}

fun Drawable.resize(@PX w: Int, @PX h: Int = w): Drawable {
    this.setBounds(0, 0, w, h)
    return this
}


fun Drawable.resizeByWidth(@PX newWidth: Int): Drawable {
    val h = this.intrinsicHeight
    val w = this.intrinsicWidth
    if (newWidth == 0 || w == 0 || w == newWidth) {
        return this
    }
    this.setBounds(0, 0, newWidth, h * newWidth / w)
    return this
}

fun Drawable.resizeByHeight(@PX newHeight: Int): Drawable {
    val h = this.intrinsicHeight
    val w = this.intrinsicWidth
    if (newHeight == 0 || h == 0 || h == newHeight) {
        return this
    }
    this.setBounds(0, 0, w * newHeight / h, newHeight)
    return this
}

fun Drawable.resizeByMaxEdge(@PX maxEdge: Int): Drawable {
    val h = this.intrinsicHeight
    val w = this.intrinsicWidth
    if (w > maxEdge || h > maxEdge) {
        val a = maxEdge
        if (w > h) {
            this.setBounds(0, 0, a, h * a / w)
        } else {
            this.setBounds(0, 0, w * a / h, a)
        }
    }
    return this
}
