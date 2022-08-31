package dev.entao.app.ui

import android.text.InputFilter
import android.text.Spanned
import android.widget.EditText
import dev.entao.app.basic.plusAssign


fun EditText.filterAcceptors(vararg acceptors: FilterAcceptor) {
    this.filters = arrayOf(AcceptFilter(acceptors.toList()))
}

class IntRangeAcceptor(private val minValue: Int, private val maxValue: Int) : FilterAcceptor {
    override fun accept(text: String): Boolean {
        val n = text.toIntOrNull() ?: return false
        return n in minValue..maxValue
    }
}

class LengthAcceptor(val maxLength: Int) : FilterAcceptor {
    override fun accept(text: String): Boolean {
        return text.length <= maxLength
    }
}

class CharSetAcceptor(val values: Set<Char>) : FilterAcceptor {
    override fun accept(text: String): Boolean {
        for (ch in text) {
            if (ch !in values) {
                return false
            }
        }
        return true
    }
}

fun interface FilterAcceptor {
    fun accept(text: String): Boolean
}

@Suppress("MemberVisibilityCanBePrivate")
class AcceptFilter(list: Collection<FilterAcceptor> = emptyList()) : InputFilter {
    val acceptors: ArrayList<FilterAcceptor> = ArrayList(list)

    override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
        val sb = StringBuilder()
        sb += dest.subSequence(0, dstart).toString()
        sb += source.subSequence(start, end).toString()
        sb += dest.subSequence(dend, dest.length).toString()
        val newValue = sb.toString()
        if (newValue.isEmpty()) {
            return null
        }
        for (a in acceptors) {
            if (!a.accept(newValue)) {
                return ""
            }
        }
        return null
    }
}