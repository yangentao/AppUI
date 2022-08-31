package dev.entao.app.ui

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.reflect.KClass


fun ViewGroup.eachChild(block: (View) -> Unit) {
    for (v in this.children) {
        block(v)
        if (v is ViewGroup) {
            v.eachChild(block)
        }
    }
}


@Suppress("UNCHECKED_CAST")
fun <T : View> ViewGroup.last(cls: KClass<T>): T? {
    return this.last {
        it::class == cls
    } as? T
}

fun ViewGroup.last(block: (View) -> Boolean): View? {
    val ls = this.children.toList().reversed()
    for (v in ls) {
        if (block(v)) {
            return v
        }
    }
    for (v in ls) {
        if (v is ViewGroup) {
            val c = v.last(block)
            if (c != null) {
                return c
            }
        }
    }
    return null
}

@Suppress("UNCHECKED_CAST")
fun <T : View> ViewGroup.first(cls: KClass<T>): T? {
    return this.first {
        it::class == cls
    } as? T
}

fun ViewGroup.first(block: (View) -> Boolean): View? {
    for (v in this.children) {
        if (block(v)) {
            return v
        }
    }
    for (v in this.children) {
        if (v is ViewGroup) {
            val c = v.first(block)
            if (c != null) {
                return c
            }
        }
    }
    return null
}

