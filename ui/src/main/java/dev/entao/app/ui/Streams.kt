package dev.entao.app.ui

import android.content.Context
import android.net.Uri
import java.io.InputStream

fun Uri.openInputStream(context: Context): InputStream? = context.contentResolver.openInputStream(this)