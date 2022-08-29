package dev.entao.app.ui

import android.view.View
import android.widget.CompoundButton

fun <T : View> T.onClick(block: () -> Unit): T {
    this.isFocusable = true
    this.setOnClickListener {
        block()
    }
    return this
}

fun <T : View> T.onClickView(block: (T) -> Unit): T {
    this.isFocusable = true
    this.setOnClickListener {
        block(this)
    }
    return this
}

fun <T : CompoundButton> T.onCheckChanged(block: (T) -> Unit): T {
    this.setOnCheckedChangeListener { _, _ ->
        block.invoke(this)
    }
    return this
}