package dev.entao.app.ui


import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.view.Gravity
import android.view.View
import dev.entao.app.basic.PX
import dev.entao.app.ui.*


class LayerBuilder {
    private val ls = ArrayList<LayerItemDrawable>()

    fun add(block: LayerItemDrawable.() -> Unit) {
        val a = LayerItemDrawable().apply(block)
        if (a.drawable != null) {
            ls += a
        }
    }

    fun add(d: Drawable, block: LayerItemDrawable.() -> Unit) {
        ls += LayerItemDrawable().apply {
            drawable = d
            this.block()
        }
    }

    operator fun plusAssign(d: Drawable) {
        this.add(d) {}
    }

    val value: LayerDrawable
        get() {
            val ld = LayerDrawable(ls.map { it.drawable }.toTypedArray())
            for (i in ls.indices) {
                val t = ls[i]
                ld.setLayerInset(i, t.leftInset, t.topInset, t.rightInset, t.bottomInset)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (t.gravity != Gravity.NO_GRAVITY) {
                        ld.setLayerGravity(i, t.gravity)
                    }

                    if (t.width >= 0) {
                        ld.setLayerWidth(i, t.width)
                    }
                    if (t.height >= 0) {
                        ld.setLayerHeight(i, t.height)
                    }
                }
                if (t.id != View.NO_ID) {
                    ld.setId(i, t.id)
                }
            }
            return ld
        }

}

class LayerItemDrawable {
    var drawable: Drawable? = null

    @PX
    var leftInset = 0

    @PX
    var topInset = 0

    @PX
    var rightInset = 0

    @PX
    var bottomInset = 0


    var gravity: Int = Gravity.NO_GRAVITY
    @PX
    var width: Int = -1
    @PX
    var height: Int = -1
    var id: Int = View.NO_ID


    fun insetX(@PX n: Int) {
        this.leftInset = n
        this.rightInset = n
    }

    fun insetY(@PX n: Int) {
        this.topInset = n
        this.bottomInset = n
    }

    fun insets(@PX n: Int) {
        this.leftInset = n
        this.rightInset = n
        this.topInset = n
        this.bottomInset = n
    }
}


fun LayerBuilder.rect(@PX leftInset: Int, @PX topInset: Int, @PX rightInset: Int, @PX bottomInset: Int, block: ShapeRect.() -> Unit) {
    add(rectShape(block)) {
        this.leftInset = leftInset
        this.rightInset = rightInset
        this.topInset = topInset
        this.bottomInset = bottomInset
    }
}

fun LayerBuilder.line(@PX leftInset: Int, @PX topInset: Int, @PX rightInset: Int, @PX bottomInset: Int, block: ShapeLine.() -> Unit) {
    add(lineShape(block)) {
        this.leftInset = leftInset
        this.rightInset = rightInset
        this.topInset = topInset
        this.bottomInset = bottomInset
    }
}

fun LayerBuilder.oval(@PX leftInset: Int, @PX topInset: Int, @PX rightInset: Int, @PX bottomInset: Int, block: ShapeOval.() -> Unit) {
    add(ovalShape(block)) {
        this.leftInset = leftInset
        this.rightInset = rightInset
        this.topInset = topInset
        this.bottomInset = bottomInset
    }
}

fun LayerBuilder.ring(@PX leftInset: Int, @PX topInset: Int, @PX rightInset: Int, @PX bottomInset: Int, block: ShapeRing.() -> Unit) {
    add(ringShape(block)) {
        this.leftInset = leftInset
        this.rightInset = rightInset
        this.topInset = topInset
        this.bottomInset = bottomInset
    }
}


fun layerDrawable(block: LayerBuilder.() -> Unit): LayerDrawable {
    val b = LayerBuilder()
    b.block()
    return b.value
}
