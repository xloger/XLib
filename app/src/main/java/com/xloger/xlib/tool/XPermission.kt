package com.xloger.xlib.tool

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat

import java.util.ArrayList

/**
 * Created on 2017/4/25 14:12.
 * Editor:xloger
 * Email:phoenix@xloger.com
 * 代码参考自：http://blog.csdn.net/lmj623565791/article/details/50709663
 */

object XPermission {
    val Read_SD = Manifest.permission.READ_EXTERNAL_STORAGE
    val Write_SD = Manifest.permission.WRITE_EXTERNAL_STORAGE
    val Camera = Manifest.permission.CAMERA
    val Read_Contacts = Manifest.permission.READ_CONTACTS
    val Audio = Manifest.permission.RECORD_AUDIO

    enum class fromClass {
        Activity, Fragment
    }

    private var callback: XPermissionCallback? = null
    private var requestCode: Int = 0

    fun requestPermission(activity: Activity, callback: XPermissionCallback, vararg perType: String) {
        XPermission.callback = callback
        request(activity, *perType)
    }

    fun requestPermission(fragment: Fragment, callback: XPermissionCallback, vararg perType: String) {
        XPermission.callback = callback
        request(fragment, *perType)
    }

    private fun request(any: Any, vararg perType: String) {
        requestCode = XTool.randomInt(100)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Xlog.log("不需要申请运行时权限")
            doExecuteSuccess(any, requestCode)
            return
        } else {
            val deniedPermissions = findDeniedPermissions(getContext(any)!!, *perType)

            if (deniedPermissions.isNotEmpty()) {
                when (any) {
                    is Activity -> any.requestPermissions(deniedPermissions.toTypedArray(), requestCode)
                    is Fragment -> any.requestPermissions(deniedPermissions.toTypedArray(), requestCode)
                    else -> throw IllegalArgumentException(any.javaClass.name + " is not supported")
                }

            } else {
                doExecuteSuccess(any, requestCode)
            }
        }

    }

    private fun doExecuteSuccess(any: Any, requestCode: Int) {
        if (callback != null) {
            callback?.onSuccess()
        } else {
            Xlog.e("权限请求回调异常")
        }
    }

    private fun doExecuteFail(any: Any, requestCode: Int, deniedPermissions: List<String>) {
        Xlog.log("申请失败的权限：$deniedPermissions")
        if (callback != null) {
            callback?.onRefuse(deniedPermissions)
        } else {
            Xlog.e("权限请求回调异常")
        }
    }

    /**
     * 默认的对权限请求失败时弹出 toast 提示。
     * 暂未支持多语言
     */
    fun defaultFailToast(context: Context, deniedPermissions: List<String>) {
        val buffer = StringBuilder()
        for (permission in deniedPermissions) {
            if (buffer.isNotEmpty()) {
                buffer.append("、")
            }
            when (permission) {
                Read_SD -> {
                    buffer.append("读取 SD 卡权限")
                }
                Write_SD -> {
                    buffer.append("写入 SD 卡权限")
                }
                Camera -> {
                    buffer.append("拍照权限")
                }
                Read_Contacts -> {
                    buffer.append("读取联系人权限")
                }
                Audio -> {
                    buffer.append("录制音频权限")
                }
                else -> buffer.append(permission)
            }
        }
        Xlog.toast(context, "请给予$buffer")
    }

    /**
     * 判断提交的权限中哪些需要申请
     */
    fun findDeniedPermissions(context: Context, vararg permission: String): List<String> {
        for (s in permission) {
            Xlog.log("试图申请的权限", s)
        }
        val denyPermissions = ArrayList<String>()
        for (value in permission) {
            if (ContextCompat.checkSelfPermission(context, value) != PackageManager.PERMISSION_GRANTED) {
                denyPermissions.add(value)
            }
        }
        Xlog.log("需要申请的权限", denyPermissions.toString())
        return denyPermissions
    }

    private fun getContext(any: Any): Context? {
        return any as? Activity ?: (any as? Fragment)?.context
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                   grantResults: IntArray, activity: Activity) {
        requestResult(activity, requestCode, permissions, grantResults)
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                   grantResults: IntArray, fragment: Fragment) {
        requestResult(fragment, requestCode, permissions, grantResults)
    }


    private fun requestResult(obj: Any, requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val deniedPermissions = ArrayList<String>()
        for (i in grantResults.indices) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permissions[i])
            }
        }
        if (deniedPermissions.size > 0) {
            doExecuteFail(obj, requestCode, deniedPermissions)
        } else {
            doExecuteSuccess(obj, requestCode)
        }
    }

    interface XPermissionCallback {
        fun onSuccess()

        fun onRefuse(deniedPermissions: List<String>)
    }
}
