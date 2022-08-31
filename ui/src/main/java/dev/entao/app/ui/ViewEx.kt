package dev.entao.app.ui

import android.content.Context
import android.graphics.Outline
import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.animation.Animation
import android.widget.ProgressBar
import androidx.core.view.children
import dev.entao.app.basic.PX
import kotlin.reflect.KClass

@PX
var View.paddingX: Int
    get() = this.paddingLeft
    set(value) {
        this.setPadding(value, this.paddingTop, value, this.paddingBottom)
    }

@PX
var View.paddingY: Int
    get() = this.paddingTop
    set(value) {
        this.setPadding(this.paddingLeft, value, this.paddingRight, value)
    }

@PX
var View.paddingAll: Int
    get() = this.paddingLeft
    set(value) {
        this.setPadding(value, value, value, value)
    }

@PX
fun View.paddingXY(x: Int, y: Int) {
    this.setPadding(x, y, x, y)
}

fun View.beginAnimation(a: Animation?, onEndCallback: () -> Unit) {
    this.animation?.cancel()
    if (a == null) {
        onEndCallback()
    } else {
        a.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                onEndCallback()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        this.startAnimation(a)
    }
}

fun View.beginAnimation(a: Animation?) {
    this.animation?.cancel()
    if (a != null) {
        this.startAnimation(a)
    }
}

fun Animation.begin(view: View) {
    this.cancel()
    view.startAnimation(this)
}

fun Animation.begin(view: View, onEndCallback: () -> Unit) {
    this.cancel()
    this.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation?) {
            onEndCallback()
        }

        override fun onAnimationRepeat(animation: Animation?) {
        }

    })
    view.startAnimation(this)
}


//corner 单位dp
class OutlineRoundRect(@PX private val corner: Int) : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        outline.setRoundRect(0, 0, view.width, view.height, corner.toFloat())
    }
}

fun <T : View> T.outlineRect(@PX corner: Int): T {
    this.outlineProvider = OutlineRoundRect(corner)
    return this
}

fun <T : View> T.gone(): T {
    visibility = View.GONE
    return this
}

fun <T : View> T.visiable(): T {
    visibility = View.VISIBLE
    return this
}


fun View.child(n: Int): View? {
    (this as? ViewGroup)?.also {
        return it.getChildAt(n)
    }
    return null
}

@Suppress("UNCHECKED_CAST")
fun <T : View> View.child(cls: KClass<T>): T? {
    (this as? ViewGroup)?.also {
        for (c in it.children) {
            if (c::class == cls) return c as T
        }
    }
    return null
}

var <T : View> T.backColor: Int
    get() {
        if (this.background is ColorDrawable) {
            return (this.background.mutate() as ColorDrawable).color
        }
        return 0
    }
    set(value) {
        setBackgroundColor(value)
    }

fun View.removeFromParent() {
    this.parentGroup?.removeView(this)
}

val View.parentGroup: ViewGroup? get() = this.parent as? ViewGroup


fun ProgressBarHor(context: Context): ProgressBar {
    return ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
}

fun ProgressBar.progressDrawables(backgroundDrawable: Drawable, progressDrawable: Drawable, secondaryProgressDrawable: Drawable) {
    this.isIndeterminate = false
    this.progressDrawable = layerDrawable {
        add {
            id = android.R.id.background
            drawable = backgroundDrawable
        }
        add {
            id = android.R.id.secondaryProgress
            drawable = ClipDrawable(secondaryProgressDrawable, Gravity.START, ClipDrawable.HORIZONTAL)
        }
        add {
            id = android.R.id.progress
            drawable = ClipDrawable(progressDrawable, Gravity.START, ClipDrawable.HORIZONTAL)
        }

    }
}

