package dev.entao.app.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView


var TextView.textValue: String
    get() {
        return this.text.toString()
    }
    set(value) {
        this.text = value
    }


fun <T : EditText> T.imeDone(block: (TextView) -> Unit): T {
    this.imeOptions = EditorInfo.IME_ACTION_DONE
    this.imeAction(EditorInfo.IME_ACTION_DONE, block)
    return this
}

fun <T : EditText> T.imeGo(block: (TextView) -> Unit): T {
    this.imeOptions = EditorInfo.IME_ACTION_GO
    this.imeAction(EditorInfo.IME_ACTION_GO, block)
    return this
}

fun <T : EditText> T.imeNext(): T {
    this.imeOptions = EditorInfo.IME_ACTION_NEXT
    return this
}

fun <T : EditText> T.imeSearch(block: (TextView) -> Unit): T {
    this.imeOptions = EditorInfo.IME_ACTION_SEARCH
    this.imeAction(EditorInfo.IME_ACTION_SEARCH, block)
    return this
}

fun <T : EditText> T.imeSend(block: (TextView) -> Unit): T {
    this.imeOptions = EditorInfo.IME_ACTION_SEND
    this.imeAction(EditorInfo.IME_ACTION_SEND, block)
    return this
}


fun <T : EditText> T.imeDone(): T {
    this.hideInputMethod()
    this.clearFocus()
    this.imeOptions = EditorInfo.IME_ACTION_DONE
    return this
}

fun <T : EditText> T.imeAction(action: Int, block: (TextView) -> Unit): T {

    this.setOnEditorActionListener(object : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (actionId == action) {
                if (v != null) {
                    v.hideInputMethod()
                    v.clearFocus()
                    block(v)
                }
                return true
            }
            return false
        }

    })
    return this
}

fun TextView.hideInputMethod() {
    if (this.isFocused) {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (imm.isActive) {
            imm.hideSoftInputFromWindow(this.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}

fun <T : EditText> T.imeSendMultiLine(block: (TextView) -> Unit): T {
    this.gravity = Gravity.TOP or Gravity.LEFT
    inputType = InputType.TYPE_CLASS_TEXT
    this.imeOptions = EditorInfo.IME_ACTION_SEND
    this.imeAction(EditorInfo.IME_ACTION_SEND, block)
    this.maxLines = 100
    setHorizontallyScrolling(false)
    return this
}

//this.textSizePx = 12.sp
var TextView.textSizePx: Float
    get() = this.textSize
    set(value) = this.setTextSize(TypedValue.COMPLEX_UNIT_PX, value)


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

@Suppress("DEPRECATION")
fun <T : TextView> T.setHtmlString(s: String) {
    if (Build.VERSION.SDK_INT >= 24) {
        this.text = Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY)
    } else {
        this.text = Html.fromHtml(s)
    }
}


fun <T : TextView> T.inputTypePassword(): T {
    this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    return this
}

fun <T : TextView> T.inputTypePasswordNumber(): T {
    this.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_PASSWORD
    return this
}

fun <T : TextView> T.inputTypePhone(): T {
    this.inputType = InputType.TYPE_CLASS_PHONE
    return this
}

fun <T : TextView> T.inputTypeEmail(): T {
    this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    return this
}

fun <T : TextView> T.inputTypeNumber(): T {
    this.inputType = InputType.TYPE_CLASS_NUMBER
    return this
}

fun <T : TextView> T.inputTypeNumberDecimal(): T {
    this.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
    return this
}


fun <T : TextView> T.singleLine(): T {
    this.isSingleLine = true
    return this
}

fun <T : TextView> T.multiLine(): T {
    this.isSingleLine = false
    return this
}

fun <T : TextView> T.ellipsizeStart(): T {
    ellipsize = TextUtils.TruncateAt.START
    return this
}

fun <T : TextView> T.ellipsizeMid(): T {
    ellipsize = TextUtils.TruncateAt.MIDDLE
    return this
}

fun <T : TextView> T.ellipsizeEnd(): T {
    ellipsize = TextUtils.TruncateAt.END
    return this
}

fun <T : TextView> T.ellipsizeMarquee(): T {
    ellipsize = TextUtils.TruncateAt.MARQUEE
    return this
}

fun <T : TextView> T.linkifyAll(): T {
    this.autoLinkMask = Linkify.ALL
    this.movementMethod = LinkMovementMethod.getInstance()
    return this
}


var <T : TextView> T.leftImage: Drawable?
    get() = this.compoundDrawables[0]
    set(value) {
        val old = this.compoundDrawables
        setCompoundDrawables(value, old[1], old[2], old[3])
    }

var <T : TextView> T.topImage: Drawable?
    get() = this.compoundDrawables[1]
    set(value) {
        val old = this.compoundDrawables
        setCompoundDrawables(old[0], value, old[2], old[3])
    }

var <T : TextView> T.rightImage: Drawable?
    get() = this.compoundDrawables[2]
    set(value) {
        val old = this.compoundDrawables
        setCompoundDrawables(old[0], old[1], value, old[3])
    }

var <T : TextView> T.bottomImage: Drawable?
    get() = this.compoundDrawables[3]
    set(value) {
        val old = this.compoundDrawables
        setCompoundDrawables(old[0], old[1], old[2], value)
    }
