package dev.entao.app.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

interface LifecycleEventCallback : LifecycleEventObserver {
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> onCreate(source)
            Lifecycle.Event.ON_START -> onStart(source)
            Lifecycle.Event.ON_RESUME -> onResume(source)
            Lifecycle.Event.ON_PAUSE -> onPause(source)
            Lifecycle.Event.ON_STOP -> onStop(source)
            Lifecycle.Event.ON_DESTROY -> onDestroy(source)
            Lifecycle.Event.ON_ANY -> {}
        }
    }

    fun onCreate(owner: LifecycleOwner) {}
    fun onStart(owner: LifecycleOwner) {}
    fun onResume(owner: LifecycleOwner) {}
    fun onPause(owner: LifecycleOwner) {}
    fun onStop(owner: LifecycleOwner) {}
    fun onDestroy(owner: LifecycleOwner) {}
}


fun LifecycleOwner.addLifeEventCallback(callback: LifecycleEventCallback) {
    this.lifecycle.addObserver(callback)
}

