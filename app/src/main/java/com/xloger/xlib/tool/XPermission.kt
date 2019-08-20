package com.xloger.xlib.tool

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat

import java.util.ArrayList
import android.provider.Settings.ACTION_SETTINGS
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.CATEGORY_DEFAULT
import android.provider.Settings
import android.provider.SyncStateContract
import com.xloger.xlib.BuildConfig
import java.lang.Exception


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

    /**
     * 当权限被拒绝时，跳转到各 ROM 的设置页
     * 代码参考于<https://blog.csdn.net/donkor_/article/details/79374442>、<https://juejin.im/entry/5a9fb306f265da239d48d936>
     * TODO 通过判断机型的方式并不准确，可能用户刷了 ROM。
     */
    public fun openPermissionConfigView(context: Context) {
        val intent = Intent()
        var comp: ComponentName?
        when (Build.MANUFACTURER) {
            ROMConstants.ROM_HUAWEI // 华为
            -> {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("packageName", BuildConfig.APPLICATION_ID)
                comp = ComponentName("com.huawei.systemmanager",
                        "com.huawei.permissionmanager.ui.MainActivity")
                intent.setComponent(comp)
            }
            ROMConstants.ROM_MEIZU // 魅族
            -> {
                intent.setAction("com.meizu.safe.security.SHOW_APPSEC")
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.putExtra("packageName", BuildConfig.APPLICATION_ID)
            }
            ROMConstants.ROM_XIAOMI // 小米
            -> {
                intent.setAction("miui.intent.action.APP_PERM_EDITOR")
                intent.setClassName("com.miui.securitycenter",
                        "com.miui.permcenter.permissions.PermissionsEditorActivity")
                intent.putExtra("extra_pkgname", context.getPackageName())
            }
            ROMConstants.ROM_SONY // 索尼
            -> {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("packageName", BuildConfig.APPLICATION_ID)
                comp = ComponentName("com.sonymobile.cta",
                        "com.sonymobile.cta.SomcCTAMainActivity")
                intent.setComponent(comp)
            }
            ROMConstants.ROM_OPPO // oppo
            -> {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("packageName", BuildConfig.APPLICATION_ID)
                comp = ComponentName("com.color.safecenter",
                        "com.color.safecenter.permission.PermissionManagerActivity")
                intent.setComponent(comp)
            }
            ROMConstants.ROM_LG // LG
            -> {
                intent.setAction("android.intent.action.MAIN")
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("packageName", BuildConfig.APPLICATION_ID)
                comp = ComponentName("com.android.settings",
                        "com.android.settings.Settings\$AccessLockSummaryActivity")
                intent.setComponent(comp)
            }
            ROMConstants.ROM_LETV // 乐视
            -> {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("packageName", BuildConfig.APPLICATION_ID)
                comp = ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps")
                intent.setComponent(comp)
            }
            else -> {
                // 跳转权限设置界面
                intent.setAction(Settings.ACTION_SETTINGS)
            }
        }
        try {
            context.startActivity(intent)
        } catch (ex: Exception) {
            Xlog.e("跳转到权限授予页失败", ex)
        }
    }

    object ROMConstants {
        val ROM_HUAWEI = "HUAWEI"
        val ROM_MEIZU = "Meizu"
        val ROM_XIAOMI = "Xiaomi"
        val ROM_SONY = "Sony"
        val ROM_OPPO = "OPPO"
        val ROM_LG = "LG"
        val ROM_LETV = "LeMobile"


//        val ROM_MIUI = "MIUI"
//        val ROM_EMUI = "EMUI"
//        val ROM_FLYME = "FLYME"
//        val ROM_OPPO = "OPPO"
//        val ROM_SMARTISAN = "SMARTISAN"
//        val ROM_VIVO = "VIVO"
//        val ROM_QIKU = "QIKU"
    }
}
