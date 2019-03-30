package com.xloger.xlib.tool

import android.content.SharedPreferences
import kotlin.properties.Delegates

/**
 * Created on 2018/5/22 15:32.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
object XConfig {
    public var isDebug = false
    private var sp : SharedPreferences by Delegates.notNull<SharedPreferences>()

    init {
        sp = XInit.applicationContext!!.getSharedPreferences("config", 0)
    }


    fun set(key: String, value: String) {
        val edit = sp.edit()
        edit.putString(key, value)
        edit.apply()
    }

    fun getString(key: String): String {
        return sp.getString(key, "")
    }

    fun set(key: String, value: Int) {
        val edit = sp.edit()
        edit.putInt(key, value)
        edit.apply()
    }

    fun getInt(key: String): Int {
        return sp.getInt(key, 0)
    }

    fun set(key: String, value: Boolean) {
        val edit = sp.edit()
        edit.putBoolean(key, value)
        edit.apply()
    }

    fun getBoolean(key: String): Boolean {
        return sp.getBoolean(key, false)
    }


}