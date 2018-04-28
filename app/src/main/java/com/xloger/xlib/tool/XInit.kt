package com.xloger.xlib.tool

import android.app.Application
import android.content.Context

/**
 * Created on 2017/9/13 11:40.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */

class XInit {
    fun init(application: Application) {
        applicationContext = application.applicationContext
    }

    companion object {
        var applicationContext: Context? = null
    }
}
