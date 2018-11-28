package com.xloger.xlib.tool

import android.os.Build
import android.util.TypedValue
import android.view.View

/**
 * Created on 2018/4/18 16:00.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
val View.selectableItemBackgroundResource: Int
    get() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            val outValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
            return outValue.resourceId
        }
        return 0
    }

val View.selectableItemBackgroundBorderlessResource: Int
    get() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            val outValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, outValue, true)
            return outValue.resourceId
        }
        return 0
    }


fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

inline fun <T> T?.notNull(block: (T) -> Unit) {
    if (this != null) {
        block(this)
    }
}