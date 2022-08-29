@file:Suppress("MemberVisibilityCanBePrivate", "unused", "ObjectPropertyName")

package dev.entao.app.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Window
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import dev.entao.app.AppInst
import dev.entao.app.basic.BlockUnit
import dev.entao.app.basic.cutby
import dev.entao.app.log.logd
import dev.entao.app.task.Msg
import dev.entao.app.task.MsgCenter
import dev.entao.app.task.MsgListener

/**
 * Created by yangentao on 16/3/12.
 */


open class BaseActivity : AppCompatActivity(), MsgListener {
    private var resultCallback: ((ActivityResult) -> Unit)? = null
    private var permCallback: ((Boolean) -> Unit)? = null
    private var permListCallback: ((Map<String, Boolean>) -> Unit)? = null

    private val permReq = this.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        val a = permCallback
        permCallback = null
        a?.invoke(it)
    }
    private val permReqList = this.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        val a = permListCallback
        permListCallback = null
        a?.invoke(it)
    }
    private val resultReq = this.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val a = this.resultCallback
        this.resultCallback = null
        a?.invoke(it)
    }

    fun requestPermission(perm: String, block: (Boolean) -> Unit) {
        this.permCallback = block
        this.permReq.launch(perm)
    }

    fun requestPermissionList(perms: Set<String>, block: (Map<String, Boolean>) -> Unit) {
        this.permListCallback = block
        this.permReqList.launch(perms.toTypedArray())
    }


    fun startActivityResult(intent: Intent, block: (ActivityResult) -> Unit) {
        resultCallback = block
        resultReq.launch(intent)
    }

    fun withPermission(perm: String, block: (Boolean) -> Unit) {
        if (this.hasPermission(perm)) {
            block(true)
            return
        }
        requestPermission(perm, block)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        AppInst.setApplicationContext(this)
        MsgCenter.add(this)
    }


    override fun onMsg(msg: Msg) {

    }


    fun statusBarColor(color: Int) {
        val w = window ?: return
        w.statusBarColor = color
    }


    override fun onDestroy() {
        MsgCenter.remove(this)
        super.onDestroy()

    }
}

fun <T : BaseActivity> T.canInstallPackage(block: (Boolean) -> Unit) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !packageManager.canRequestPackageInstalls()) {
        startActivityResult(Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, Uri.parse("package:$packageName"))) {
            block(it.OK && packageManager.canRequestPackageInstalls())
        }
        return
    }
    block(true)
}

val ActivityResult.OK: Boolean get() = this.resultCode == Activity.RESULT_OK


fun BaseActivity.onPermission(perm: String, block: BlockUnit) {
    if (this.hasPermission(perm)) {
        block()
    } else {
        requestPermission(perm) {
            if (it) {
                block()
            }
        }

    }
}

fun BaseActivity.onPermissions(perms: Set<String>, block: BlockUnit) {
    if (this.hasPermissions(perms)) {
        block()
    } else {
        requestPermissionList(perms) { r ->
            if (r.values.all { it }) {
                block()
            }
        }
    }
}


//[0-1.0]
@SuppressLint("DiscouragedPrivateApi")
fun Context.setVolumePercent(percent: Float) {
    val p: Float = percent cutby (0f to 1f)
    val am: AudioManager = this.getSystemService(Service.AUDIO_SERVICE) as AudioManager
    val maxV = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    val v: Int = (maxV * p).toInt() cutby (0 to maxV)

    logd("max volume: ", maxV)
    am.setStreamVolume(AudioManager.STREAM_MUSIC, v, 0)
}


fun BaseActivity.openDir(block: (DocumentFile) -> Unit) {
    val act = this
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
    startActivityResult(intent) { ar ->
        if (ar.OK) {
            val u = ar.data?.data
            if (u != null) {
                block(DocumentFile.fromTreeUri(act, u)!!)
            }
        }
    }
}

fun BaseActivity.openFile(mime: String, block: (DocumentFile?) -> Unit) {
    val act = this
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.type = mime
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    startActivityResult(intent) { ar ->
        if (ar.OK) {
            val u = ar.data?.data
            if (u != null) {
                block(DocumentFile.fromSingleUri(act, u))
            } else {
                block(null)
            }
        }
    }
}