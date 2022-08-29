@file:Suppress("FunctionName")

package dev.entao.app.ui

import android.os.Build
import android.os.StatFs
import dev.entao.app.basic.makeTempName
import java.io.File
import java.io.IOException


val DocDir: File
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        appContext.dataDir
    } else {
        appContext.filesDir
    }


fun DocFile(name: String): File {
    return File(DocDir, name)
}

fun DocFile(subdir: String, name: String): File {
    val d = File(DocDir, subdir)
    if (!d.exists()) {
        d.mkdir()
    }
    return File(d, name)
}


val CacheDir: File get() = appContext.externalCacheDir ?: appContext.cacheDir

fun CacheFile(name: String): File {
    return File(CacheDir, name)
}

fun CacheFile(subdir: String, name: String): File {
    val d = File(CacheDir, subdir)
    if (!d.exists()) {
        d.mkdir()
        d.nomedia()
    }
    return File(d, name)
}

fun TempFile(ext: String = "tmp"): File {
    return CacheFile(makeTempName(ext))
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

