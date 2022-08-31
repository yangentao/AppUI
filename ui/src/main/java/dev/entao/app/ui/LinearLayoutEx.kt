package dev.entao.app.ui

import android.widget.LinearLayout


fun <T : LinearLayout> T.isVertical(): Boolean {
    return this.orientation == LinearLayout.VERTICAL
}

fun <T : LinearLayout> T.horizontal(): T {
    this.orientation = LinearLayout.HORIZONTAL
    return this
}

fun <T : LinearLayout> T.vertical(): T {
    this.orientation = LinearLayout.VERTICAL
    return this
}
