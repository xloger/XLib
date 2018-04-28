package com.xloger.test

import android.app.Application
import com.xloger.xlib.tool.XInit

/**
 * Created on 2018/4/28 14:46.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        XInit().init(this)
    }
}