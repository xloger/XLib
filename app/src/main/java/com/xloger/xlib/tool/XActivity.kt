package com.xloger.xlib.tool

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.ctx
import org.jetbrains.anko.internals.AnkoInternals

/**
 * Created on 2017/7/10 11:40.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */

open class XActivity : AppCompatActivity() {

    @JvmOverloads
    fun startActivity(context: Context, cls: Class<*>, extras: Bundle? = null) {
        val intent = Intent()
        intent.setClass(context, cls)
        if (extras != null) {
            intent.putExtras(extras)
        }
        if (context !is Activity) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            Xlog.toast(context, "未能找到可用以打开网页的应用")
        }

    }

    fun setStatusBarColor(@ColorRes id : Int) {
        StatusBarColorCompat.setStatusBarColor(this, ContextCompat.getColor(ctx, id))
    }


    inline fun <reified T: Activity> startActivity(vararg params: Pair<String, Any>) {
        AnkoInternals.internalStartActivity(this, T::class.java, params)
    }

}
