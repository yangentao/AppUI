package dev.entao.app.ui

import android.widget.LinearLayout
import dev.entao.app.viewbuilder.Grav


fun LinearLayout.grav(block: Grav.() -> Unit): LinearLayout {
    Grav { g -> this.gravity = g }.apply(block)
    return this
}