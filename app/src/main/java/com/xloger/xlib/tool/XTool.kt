package com.xloger.xlib.tool

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Looper
import android.os.Vibrator
import java.util.*
import android.content.Context.ACTIVITY_SERVICE



/**
 * Created on 2017/5/3 10:33.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */

object XTool {

    val isOnMainThread: Boolean
        get() = Thread.currentThread() === Looper.getMainLooper().thread

    fun isBlankString(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' } == ""
    }

    /**
     * 返回一个[1,max]的int
     *
     * @param max 大于等于1的正整数
     * @return
     */
    fun randomInt(max: Int): Int {
        return (Math.random() * max).toInt() + 1
    }


    /**
     * 打印当前时间，可选传入说明
     * 主要用途是方便观察哪里耗时了
     */
    fun time(description: String = "") : String {
        val date = Date(System.currentTimeMillis())
        println(date.toString())
        Xlog.debug("$description 时间：$date")
        return "${description}${date}"
    }

    /**
     * 震动手机，需要对应权限
     */
    @SuppressLint("MissingPermission")
    fun vibrate(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }

    fun getSelfPacakgeName(mContext: Context): String {
        try {
            val am = mContext
                    .getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager
            val cn = am.getRunningTasks(1).get(0).topActivity
            return cn.getPackageName()
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }

    }


}
