@file:Suppress("EnumEntryName", "unused")

package dev.entao.app.ui

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import dev.entao.app.resDrawable

fun listColors(normal: Int? = null, block: StateIntBuilder.() -> Unit): ColorStateList {
    val b = StateIntBuilder(normal)
    b.block()
    return b.colorList
}

fun listColorDrawables(normal: Int? = null, block: StateIntBuilder.() -> Unit): StateListDrawable {
    return StateIntBuilder(normal).apply(block).colorDrawables
}


fun listDrawables(normal: Drawable? = null, block: StateDrawableBuilder.() -> Unit): StateListDrawable {
    return StateDrawableBuilder(normal).apply(block).drawableList
}


fun listResDrawables(normal: Int? = null, block: StateIntBuilder.() -> Unit): StateListDrawable {
    return StateIntBuilder(normal).apply(block).resDrawables
}


enum class States(val value: Int) {
    selected(android.R.attr.state_selected), unselected(-android.R.attr.state_selected),
    pressed(android.R.attr.state_pressed), unpressed(-android.R.attr.state_pressed),
    enabled(android.R.attr.state_enabled), disabled(-android.R.attr.state_enabled),
    checked(android.R.attr.state_checked), unchecked(-android.R.attr.state_checked),
    focused(android.R.attr.state_focused), unfocused(-android.R.attr.state_focused),
    normal(0)
}

typealias StateIntBuilder = StateBuilder<Int>
typealias StateDrawableBuilder = StateBuilder<Drawable>

val StateDrawableBuilder.drawableList: StateListDrawable
    get() {
        val ld = StateListDrawable()
        for (p in values) {
            ld.addState(p.first.toIntArray(), p.second)
        }
        return ld
    }


val StateIntBuilder.colorList: ColorStateList
    get() {
        val ls = values
        val a: Array<IntArray> = ls.map { it.first.toIntArray() }.toTypedArray()
        val b: IntArray = ls.map { it.second }.toIntArray()
        return ColorStateList(a, b)
    }

val StateIntBuilder.colorDrawables: StateListDrawable
    get() {
        val ld = StateListDrawable()
        for (p in values) {
            ld.addState(p.first.toIntArray(), ColorDrawable(p.second))
        }
        return ld
    }
val StateIntBuilder.resDrawables: StateListDrawable
    get() {
        val ld = StateListDrawable()
        for (p in values) {
            ld.addState(p.first.toIntArray(), p.second.resDrawable)
        }
        return ld
    }

class StateBuilder<T>(var normalValue: T? = null) {
    private val items: ArrayList<Pair<List<Int>, T>> = ArrayList()

    val values: List<Pair<List<Int>, T>>
        get() {
            val nv = normalValue
            return if (nv == null) {
                items
            } else {
                items + (listOf(States.normal.value) to nv)
            }
        }


    fun states(v: T, vararg stateList: States) {
        if (stateList.isNotEmpty()) {
            normalValue = v
        } else {
            items += stateList.map { it.value } to v
        }
    }

    fun states(vararg stateList: States, block: () -> T) {
        val v = block()
        if (stateList.isNotEmpty()) {
            normalValue = v
        } else {
            items += stateList.map { it.value } to v
        }
    }

    fun normal(v: T): StateBuilder<T> {
        this.normalValue = v
        return this
    }

    fun normal(block: () -> T) {
        this.normalValue = block()
    }

    fun lighted(v: T): StateBuilder<T> {
        pressed(v)
        selected(v)
        checked(v)
        focused(v)
        return this
    }

    fun lighted(block: () -> T) {
        lighted(block())
    }


    fun selected(v: T): StateBuilder<T> {
        items += listOf(States.selected.value) to v
        return this
    }

    fun selected(block: () -> T) {
        selected(block())
    }

    fun unselected(v: T): StateBuilder<T> {
        items += listOf(States.unselected.value) to v
        return this
    }

    fun unselected(block: () -> T) {
        unselected(block())
    }

    fun pressed(v: T): StateBuilder<T> {
        items += listOf(States.pressed.value) to v
        return this
    }

    fun pressed(block: () -> T) {
        pressed(block())
    }

    fun unpressed(v: T): StateBuilder<T> {
        items += listOf(States.unpressed.value) to v
        return this
    }

    fun unpressed(block: () -> T) {
        unpressed(block())
    }

    fun enabled(v: T): StateBuilder<T> {
        items += listOf(States.enabled.value) to v
        return this
    }

    fun enabled(block: () -> T) {
        enabled(block())
    }

    fun disabled(v: T): StateBuilder<T> {
        items += listOf(States.disabled.value) to v
        return this
    }

    fun disabled(block: () -> T) {
        disabled(block())
    }

    fun checked(v: T): StateBuilder<T> {
        items += listOf(States.checked.value) to v
        return this
    }

    fun checked(block: () -> T) {
        checked(block())
    }

    fun unchecked(v: T): StateBuilder<T> {
        items += listOf(States.unchecked.value) to v
        return this
    }

    fun unchecked(block: () -> T) {
        unchecked(block())
    }

    fun focused(v: T): StateBuilder<T> {
        items += listOf(States.focused.value) to v
        return this
    }

    fun focused(block: () -> T) {
        focused(block())
    }

    fun unfocused(v: T): StateBuilder<T> {
        items += listOf(States.unfocused.value) to v
        return this
    }

    fun unfocused(block: () -> T) {
        unfocused(block())
    }
}
