package dev.entao.app.ui

import android.content.res.ColorStateList
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import dev.entao.app.viewbuilder.Grav
import java.lang.ref.WeakReference



open class XTextWatcher : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

    }

    override fun afterTextChanged(s: Editable) {
        afterChanged(s.toString())
    }

    open fun afterChanged(text: String) {

    }
}

fun TextView.grav(block: Grav.() -> Unit): TextView {
    Grav { g -> this.gravity = g }.apply(block)
    return this
}

var TextView.textColorValue: Int
    get() = this.currentTextColor
    set(value) {
        this.setTextColor(value)
    }

var TextView.textColorList: ColorStateList?
    get() = this.textColors
    set(value) {
        this.setTextColor(value)
    }

fun <T : TextView> T.afterChanged(block: (String) -> Unit): T {
    this.addTextChangedListener(object : XTextWatcher() {
        override fun afterChanged(text: String) {
            block(text)
        }

    })
    return this
}

fun <T : TextView> T.afterChangedView(block: (TextView, String) -> Unit): T {
    val tv = this
    this.addTextChangedListener(object : XTextWatcher() {
        override fun afterChanged(text: String) {
            block(tv, text)
        }

    })
    return this
}

fun <T : EditText> T.setMaxLength(m: Int) {
    val ed = this
    this.addTextChangedListener(object : XTextWatcher() {
        private var ts: WeakReference<Toast>? = null

        override fun afterTextChanged(s: Editable) {
            if (s.length > m) {
                ts?.get()?.cancel()
                val t = ed.context.toastShort("最多 $m 个字符")
                ts = WeakReference(t)
                s.delete(ed.selectionStart - 1, ed.selectionEnd)
                ed.setTextKeepState(s)
            }
        }
    })
}
