package com.xloger.test

import android.util.Log
import java.net.URL

/**
 * Created on 2017/10/10 17:30.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */
public class Request(val url: String) {
    public fun run() {
        val forecastJsonStr = URL(url).readText()
        Log.d(javaClass.simpleName, forecastJsonStr)
    }
}