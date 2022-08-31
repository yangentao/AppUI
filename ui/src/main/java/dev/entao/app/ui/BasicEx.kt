package dev.entao.app.ui

import java.text.Collator
import java.util.*


fun printX(vararg vs: Any?) {
    val s = vs.joinToString(" ") {
        it?.toString() ?: "null"
    }
    println(s)
}

//TODO delete hexText
//val UUID.hexText: String get() = String.format("%x%016x", this.mostSignificantBits, this.leastSignificantBits)
val collatorChina: Collator by lazy { Collator.getInstance(Locale.CHINA) }
val chinaComparator = Comparator<String> { left, right -> collatorChina.compare(left, right) }

class ChinaComparator<T>(val block: (T) -> String) : java.util.Comparator<T> {
    override fun compare(o1: T, o2: T): Int {
        return chinaComparator.compare(block(o1), block(o2))
    }
}

//TODO delete makeTempName
//fun makeTempName(ext: String = "tmp"): String {
//    val dotExt = when {
//        ext.isEmpty() -> ""
//        ext.startsWith(".") -> ext
//        else -> ".$ext"
//    }
//    return UUID.randomUUID().hexText + dotExt
//}

object IDGen {
    private var id = 0

    @Synchronized
    fun next(): Int {
        return ++id
    }
}