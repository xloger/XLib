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
    private var isDebug = true
    private var isAlwaysShowInvoke = false
    private var TAG = "Xlog>>"
    private var LogSize = 4000

    private var toast: Toast? = null
    private var lastTime: Long = 0

    /**
     * 设置是否为 debug 模式。
     * 只有当该值为 true 时才输出日志，为 false 时只有当产生 error 日志时会输出。
     * 推荐 debug 包该值为 true，release 包为 false.
     * eg: if(BuildConfig.DEBUG) { Xlog.isDebug = true } else { Xlog.isDebug = false }
     */
    fun isDebug(isDebug: Boolean) {
        this.isDebug = isDebug
    }

    /**
     * 设置为始终显示调用的堆栈信息
     */
    fun setAlwaysShowInvoke() {
        isAlwaysShowInvoke = true
    }

    /**
     * 设置 TAG 的基础名字
     */
    fun setTag(tag: String) {
        TAG = tag
    }

    /**
     * 设置一条 Log 最长的尺寸。
     * 因为控制台对日志的最长尺寸有要求，不得超过 4 * 1024，因此对过长的日志会自动拆分。该参数可以控制默认的尺寸限制，默认为 4000.
     */
    fun setLogSize(size: Int = 4000) {
        LogSize = size
    }

    fun log(text: String) {
        log("", text)
    }

    /**
     * 记录展示数据，以 info 层级输出。
     * @param tag log 的分组，可以传入当前 class，也可以以功能区分
     * @param text 要打印的内容
     */
    fun log(tag: String, text: String) {
        Logcat.log(TAG + tag, text, isAlwaysShowInvoke, "i")
    }

    fun logWithInvoke(tag: String, text: String) {
        Logcat.log(TAG + tag, text, true, "i")
    }


    /**
     * 记录展示数据，以 debug 层级输出。推荐临时测试时使用，事后删除。
     * @param text 要打印的内容
     */
    fun debug(text: String) {
        Logcat.log(TAG, text, isAlwaysShowInvoke, "d")
    }



    /**
     * 记录展示可能发生的异常，以 error 层级输出。
     * TODO 将其统计并存为日志或者发送给服务器
     * @param tag 异常的类型
     * @param text 异常的描述信息
     * @param tr 异常本身
     */
    fun e(tag: String, text: String, tr: Throwable?) {
        Logcat.log(TAG + tag, "【$text】" + tr, true, "e")
    }

    fun e(text: String) {
        e("", text, null)
    }

    fun e(text: String, tr: Throwable?) {
        e("", text, tr)
    }

    /**
     * 日志功能的实现类
     */
    private class Logcat {
        companion object {
            /**
             * 调用信息。目前该方案获取的好像不是很准确。
             */
            private val invokeInfo: String
                get() {
                    val elements = Thread.currentThread().stackTrace
                    val element = elements[5]
                    return String.format("(%s:%d)#%s ", element.fileName, element.lineNumber, element.methodName)
                }

            @JvmOverloads
            fun log(tag: String, text: String, isShowInvoke: Boolean = false, level: String = "d") {
                var invoke = ""
                if (isShowInvoke) {
                    invoke = invokeInfo
                }
                //日志长度上限为 4 * 1024，因此超过长度则需拆分。
                if (text.length < LogSize) {
                    print(tag, invoke + text, level)
                } else {
                    val list = text.chunked(LogSize)
                    list.forEachIndexed { index, s ->
                        print("$tag [$index]", if (index == 0) invoke + s else s, level)
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
        }

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
//            Xlog.e("没有在主线程调用 toast")
            Looper.prepare()
            //            return;
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

    /**
     * 封装的一个 dialog 方法，很不成熟很不好用，目前我自己一直使用 Anko 提供的 dialog 方法了。
     * 但是 Anko 存在的一个 bug 还没修复（https://github.com/Kotlin/anko/issues/731），如果再不修复我可能自己重构一下继续用这个了。
     */
    @Deprecated("写得太菜，完全比不过 Anko 提供的方法。")
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
