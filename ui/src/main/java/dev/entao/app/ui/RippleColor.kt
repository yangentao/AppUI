package dev.entao.app.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import dev.entao.app.basic.PX


object RippleColor {
    var disabledBackColor: Int = 0x606060.rgb

    fun fill(color: Int, @PX corner: Int): RippleDrawable {
        return build(color, corner) {
            listDrawables {
                normal {
                    ShapeRect(color, corner).value
                }
                disabled {
                    ShapeRect(disabledBackColor, corner).value
                }
            }
        }
    }

    fun stroke(fillColor: Int, @PX corner: Int, strokeColor: Int, @PX strokeWidth: Int): RippleDrawable {
        return build(strokeColor, corner) {
            listDrawables {
                normal {
                    rectShape {
                        this.fill(fillColor)
                        corner(corner)
                        strokePx(strokeWidth, strokeColor)
                    }
                }
                disabled {
                    rectShape {
                        fill(fillColor)
                        corner(corner)
                        strokePx(strokeWidth, disabledBackColor)
                    }
                }
            }
        }
    }

    fun build(primaryColor: Int, @PX corner: Int, contentCallback: () -> Drawable): RippleDrawable {
        val mask: Drawable = ShapeRect(primaryColor, corner).drawable
        val ripple: ColorStateList = listColors(Color.TRANSPARENT) {
            lighted(primaryColor.darkColor)
            disabled(disabledBackColor)
        }
        return RippleDrawable(ripple, contentCallback(), mask)
    }
}
