@file:Suppress("FunctionName")

package dev.entao.app.ui

import android.content.Context
import android.os.Build
import android.os.StatFs
import dev.entao.app.basic.makeTempName
import java.io.File
import java.io.IOException


fun DocDir(context: Context): File = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
    context.dataDir
} else {
    context.filesDir
}


fun DocFile(context: Context, name: String): File {
    return File(DocDir(context), name)
}

fun DocFile(context: Context, subdir: String, name: String): File {
    val d = File(DocDir(context), subdir)
    if (!d.exists()) {
        d.mkdir()
    }
    return File(d, name)
}


fun CacheDir(context: Context): File = context.externalCacheDir ?: context.cacheDir

fun CacheFile(context: Context, name: String): File {
    return File(CacheDir(context), name)
}

fun CacheFile(context: Context, subdir: String, name: String): File {
    val d = File(CacheDir(context), subdir)
    if (!d.exists()) {
        d.mkdir()
        d.nomedia()
    }
    return File(d, name)
}

fun TempFile(context: Context, ext: String = "tmp"): File {
    return CacheFile(context, makeTempName(ext))
}


fun File.nomedia() {
    if (!this.isDirectory) return
    try {
        val f = File(this, ".nomedia")
        if (!f.exists()) {
            f.createNewFile()
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

val File.fileList: List<File>
    get() = this.listFiles()?.toList() ?: emptyList()

val File.totalSize: Long
    get() {
        if (!this.exists()) return 0L
        if (this.isFile) return this.length()
        return this.fileList.sumOf { it.totalSize }
    }


val File.statFS: StatFs get() = StatFs(this.absolutePath)

fun File.listFiles(deep: Boolean): List<File> {
    val allList = ArrayList<File>()
    this.listFilesTo(allList, deep)
    return allList
}

fun File.listFilesTo(list: ArrayList<File>, deep: Boolean) {
    if (this.isFile) return
    val ls = this.fileList
    list.addAll(ls)
    if (deep) {
        for (f in list) {
            f.listFilesTo(list, deep)
        }
    }
}

