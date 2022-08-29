package dev.entao.app.ui


import android.graphics.drawable.GradientDrawable
import dev.entao.app.basic.PX


open class ShapeBase(shape: Int) {
    val value: GradientDrawable = GradientDrawable()
    val drawable: GradientDrawable get() = value

    init {
        value.shape = shape
    }


    //andle , 0, 90, 180, 270
    fun gradient(colorStart: Int, colorEnd: Int, angle: Int = 0): ShapeBase {
        value.colors = intArrayOf(colorStart, colorEnd)
        value.orientation = when (angle) {
            45 -> GradientDrawable.Orientation.BL_TR
            90 -> GradientDrawable.Orientation.BOTTOM_TOP
            135 -> GradientDrawable.Orientation.BR_TL
            180 -> GradientDrawable.Orientation.RIGHT_LEFT
            225 -> GradientDrawable.Orientation.TR_BL
            270 -> GradientDrawable.Orientation.TOP_BOTTOM
            315 -> GradientDrawable.Orientation.TL_BR
            else -> GradientDrawable.Orientation.LEFT_RIGHT
        }
        return this
    }

    //andle , 0, 90, 180, 270
    fun gradient(colors: IntArray, angle: Int = 0): ShapeBase {
        value.colors = colors
        value.orientation = when (angle) {
            45 -> GradientDrawable.Orientation.BL_TR
            90 -> GradientDrawable.Orientation.BOTTOM_TOP
            135 -> GradientDrawable.Orientation.BR_TL
            180 -> GradientDrawable.Orientation.RIGHT_LEFT
            225 -> GradientDrawable.Orientation.TR_BL
            270 -> GradientDrawable.Orientation.TOP_BOTTOM
            315 -> GradientDrawable.Orientation.TL_BR
            else -> GradientDrawable.Orientation.LEFT_RIGHT
        }
        return this
    }

    fun fill(color: Int): ShapeBase {
        value.setColor(color)
        return this
    }

    fun stroke(@PX width: Int, color: Int): ShapeBase {
        value.setStroke(width, color)
        return this
    }

    fun strokePx(@PX width: Int, color: Int): ShapeBase {
        value.setStroke(width, color)
        return this
    }

    fun strokeDash(@PX width: Int, color: Int, @PX dashWidth: Int, @PX dashGap: Int): ShapeBase {
        value.setStroke(width, color, dashWidth.toFloat(), dashGap.toFloat())
        return this
    }

    fun alpha(n: Int): ShapeBase {
        value.alpha = n
        return this
    }

    fun size(@PX w: Int, @PX  h: Int = w): ShapeBase {
        value.setSize(w, h)
        return this
    }

    fun topToBottom(): ShapeBase {
        value.orientation = GradientDrawable.Orientation.TOP_BOTTOM
        return this
    }

    fun leftToRight(): ShapeBase {
        value.orientation = GradientDrawable.Orientation.LEFT_RIGHT
        return this
    }
}

class ShapeRect() : ShapeBase(GradientDrawable.RECTANGLE) {


    constructor(fillColor: Int) : this() {
        this.fill(fillColor)
    }

    constructor(fillColor: Int, @PX corner: Int) : this() {
        this.fill(fillColor)
        this.corner(corner)
    }


    fun corner(@PX corner: Int): ShapeRect {
        value.cornerRadius = corner.toFloat()
        return this
    }

    fun cornerPx(@PX corner: Int): ShapeRect {
        value.cornerRadius = corner.toFloat()
        return this
    }

    @PX
    fun corners(topLeft: Int, topRight: Int, bottomRight: Int, bottomLeft: Int): ShapeRect {
        val f1 = topLeft.toFloat()
        val f2 = topRight.toFloat()
        val f3 = bottomRight.toFloat()
        val f4 = bottomLeft.toFloat()
        value.cornerRadii = floatArrayOf(f1, f1, f2, f2, f3, f3, f4, f4)
        return this
    }


}


class ShapeLine : ShapeBase(GradientDrawable.LINE)

class ShapeOval() : ShapeBase(GradientDrawable.OVAL) {
    constructor(fillColor: Int) : this() {
        fill(fillColor)
    }
}

class ShapeRing : ShapeBase(GradientDrawable.RING)

fun rectShape(block: ShapeRect.() -> Unit): GradientDrawable {
    val r = ShapeRect()
    r.block()
    return r.value
}

fun ovalShape(block: ShapeOval.() -> Unit): GradientDrawable {
    val a = ShapeOval()
    a.block()
    return a.value
}

//画虚线需要关闭硬件加速: view.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
fun lineShape(block: ShapeLine.() -> Unit): GradientDrawable {
    val a = ShapeLine()
    a.block()
    return a.value
}


fun ringShape(block: ShapeRing.() -> Unit): GradientDrawable {
    val a = ShapeRing()
    a.block()
    return a.value
}

