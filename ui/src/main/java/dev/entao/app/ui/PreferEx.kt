package dev.entao.app.ui

import android.content.Context
import androidx.core.content.edit
import dev.entao.app.basic.nameWithClass
import dev.entao.app.basic.userName
import kotlin.reflect.KProperty

/**
 * only support String/Int/Long/Boolean
 */
@Suppress("UNCHECKED_CAST")
class PreferValue(val context: Context, filename: String = "gloabl_prefer") {
    private val prefer = context.getSharedPreferences(filename, 0)

    operator fun <T : Any> getValue(thisRef: Any?, property: KProperty<*>): T {
        return when (property.returnType.classifier) {
            String::class -> prefer.getString(property.userName, "")
            Int::class -> prefer.getInt(property.userName, 0)
            Long::class -> prefer.getLong(property.userName, 0L)
            Boolean::class -> prefer.getBoolean(property.userName, false)
            else -> error("PreferValue, Unsupport type: ${property.nameWithClass}")
        } as T
    }

    operator fun <T : Any> setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        when (property.returnType.classifier) {
            String::class -> prefer.edit { putString(property.userName, value as String) }
            Int::class -> prefer.edit { putInt(property.userName, value as Int) }
            Long::class -> prefer.edit { putLong(property.userName, value as Long) }
            Boolean::class -> prefer.edit { putBoolean(property.userName, value as Boolean) }
            else -> error("PreferValue, Unsupport type: ${property.nameWithClass}")
        }
    }
}