package com.xloger.xlib.tool

import android.os.Looper
import android.support.v7.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast

/**
 * Created on 2017/4/11 16:26.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */

object Xlog {
    public var isDebug = true
    private var isAlwaysShowInvoke = false
    private val TAG = "Xlog>>"
    //TODO 配置成可供修改的
    private var LogSize = 4000
    private var toast: Toast? = null
    private var lastTime: Long = 0

    //        StackTraceElement targetElement = new Throwable().getStackTrace()[3];
    //        String className = targetElement.getClassName();
    //        String ret = new Formatter()
    //                .format("%s, %s(%s.java:%d)",
    //                        Thread.currentThread().getName(),
    //                        targetElement.getMethodName(),
    //                        className,
    //                        targetElement.getLineNumber())
    //                .toString();
    private val invokeInfo: String
        get() {
            val elements = Thread.currentThread().stackTrace
            val element = elements[5]
            return String.format("(%s:%d)#%s ", element.fileName, element.lineNumber, element.methodName)
        }

    fun setAlwaysShowInvoke() {
        isAlwaysShowInvoke = true
    }

    fun log(text: String) {
        log("", text)
    }

    fun logWithInvoke(tag: String, text: String) {
        log(tag, text, true)
    }

    @JvmOverloads
    fun log(tag: String, text: String, isShowInvoke: Boolean = false, level: String = "d") {
        var invoke = ""
        if (isShowInvoke || isAlwaysShowInvoke) {
            invoke = invokeInfo
        }
        if (isDebug) {
            val printTag = "$TAG $tag"
            //日志长度上限为 4 * 1024，因此超过长度则需拆分。
            if (text.length < LogSize) {
                print(printTag, invoke + text, level)
            } else {
                val list = text.chunked(LogSize)
                list.forEachIndexed { index, s ->
                    print(printTag + index, if (index == 0) invoke + s else s, level)
                }
            }
        }
    }

    private fun print(tag: String, content: String, level: String) {
        when (level) {
            "v" -> Log.v(tag, content)
            "d" -> Log.d(tag, content)
            "i" -> Log.i(tag, content)
            "w" -> Log.w(tag, content)
            "e" -> Log.e(tag, content)
        }
    }

    fun debug(text: String) {
        log(text)
    }

    fun e(text: String) {
        e("", text)
    }

    fun e(tag: String, text: String) {
        log(tag, text, true, "e")
    }

    fun e(tag: String, tr: Throwable) {
        e(tag, tr.message ?: "")
    }

    fun toast(text: String) {
        val context = XInit.applicationContext
        if (context != null) {
            toast(context, text)
        } else {
            Xlog.e("XInit 初始化异常")
        }
    }

    /**
     * 在主线程中调用时，会弹出一个 toast，且多次点击会即时更新内容。
     * 在非主线程调用时，可以弹出 toast，但是没法即时修改内容。
     */
    fun toast(context: Context, text: String) {
        if (!XTool.isOnMainThread) {
            Xlog.e("没有在主线程调用 toast")
            Looper.prepare()
            //            return;
        }

        if (context == null) {
            Xlog.e("context == null")
            return
        }

        val nowTime = System.currentTimeMillis()
        if (toast != null && nowTime - lastTime < 2000) {
            toast!!.setText(text)
        } else {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
            toast!!.show()
            lastTime = nowTime
        }

        log("toast", text)
        if (!XTool.isOnMainThread) {
            Looper.loop()
        }
    }

    fun dialog(context: Context, title: String, message: String, yesText: String, noText: String, listener: DialogOnClickListener) {
        dialog(context, title, message, yesText, noText, "", listener)
    }

    fun dialog(context: Context, title: String, message: String, yesText: String, noText: String, neutralText: String, listener: DialogOnClickListener) {
        if (!XTool.isOnMainThread) {
            Xlog.e("没有在主线程调用 dialog")
            return
        }
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(yesText) { dialog, which -> listener.onPositiveButtonClick(dialog, which) }
        builder.setNegativeButton(noText) { dialog, which -> listener.onNegativeButtonClick(dialog, which) }
        builder.setNeutralButton(neutralText) { dialog, which -> listener.onNeutralButtonClick(dialog, which) }
        builder.create().show()
    }


    interface DialogOnClickListener {
        fun onPositiveButtonClick(dialog: DialogInterface, which: Int)

        fun onNegativeButtonClick(dialog: DialogInterface, which: Int)

        fun onNeutralButtonClick(dialog: DialogInterface, which: Int)

    }
}
