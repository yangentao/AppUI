package dev.entao.app.ui

import android.graphics.drawable.ColorDrawable
import android.view.View

var <T : View> T.backColor: Int
    get() {
        if (this.background is ColorDrawable) {
            return (this.background.mutate() as ColorDrawable).color
        }
        return 0
    }
    set(value) {
        setBackgroundColor(value)
    }

