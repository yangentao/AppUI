package dev.entao.app.ui

import android.view.Gravity
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView


val <T : TextView> T.grav: Grav get() = Grav { g -> this.gravity = g }
val <T : LinearLayout> T.grav: Grav get() = Grav { g -> this.gravity = g }
val <T : FrameLayout.LayoutParams> T.grav: Grav get() = Grav { g -> this.gravity = g }
val <T : LinearLayout.LayoutParams> T.grav: Grav get() = Grav { g -> this.gravity = g }

class Grav(private val setter: (Int) -> Unit) {
    private var g: Int = Gravity.NO_GRAVITY
    fun center(): Grav {
        g = g or Gravity.CENTER
        setter(g)
        return this
    }

    fun centerX(): Grav {
        g = g or Gravity.CENTER_HORIZONTAL
        setter(g)
        return this
    }

    fun centerY(): Grav {
        g = g or Gravity.CENTER_VERTICAL
        setter(g)
        return this
    }

    fun fill(): Grav {
        g = g or Gravity.FILL
        setter(g)
        return this
    }

    fun fillX(): Grav {
        g = g or Gravity.FILL_HORIZONTAL
        setter(g)
        return this
    }

    fun fillY(): Grav {
        g = g or Gravity.FILL_VERTICAL
        setter(g)
        return this
    }

    fun top(): Grav {
        g = g or Gravity.TOP
        setter(g)
        return this
    }

    fun bottom(): Grav {
        g = g or Gravity.BOTTOM
        setter(g)
        return this
    }

    fun left(): Grav {
        g = g or Gravity.LEFT
        setter(g)
        return this
    }

    fun right(): Grav {
        g = g or Gravity.RIGHT
        setter(g)
        return this
    }

    fun start(): Grav {
        g = g or Gravity.START
        setter(g)
        return this
    }

    fun end(): Grav {
        g = g or Gravity.END
        setter(g)
        return this
    }

}

