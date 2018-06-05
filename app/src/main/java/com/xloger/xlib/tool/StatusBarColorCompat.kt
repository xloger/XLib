package com.xloger.xlib.tool

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.FrameLayout

/**
 * 代码来自：https://www.ctolib.com/amp/StatusBarColorCompat.html
 * 我还没太懂引用 License，因此暂未申明来源
 */
object StatusBarColorCompat {

    val IS_KITKAT = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP
    val IS_LOLLIPOP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
    /** 只在KITKAT下有用到  */
    val KITKAT_STATUSBAR_HEIGHT = if (IS_KITKAT) statusBarHeight else 0
    private val KITKAT_FAKE_STATUSBAR_BACKGROUND_VIEW_TAG = "KITKAT_FAKE_STATUSBAR_BACKGROUND_VIEW_TAG"

    // 一般不可能没有这个id，除非这个厂商是傻逼。
    // 如果没有取到，则取25dp，貌似6.0以上是24dp？这个暂时懒得管了
    val statusBarHeight: Int
        get() {
            val res = Resources.getSystem()
            val id = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android")
            return if (id > 0) {
                res.getDimensionPixelSize(id)
            } else (res.displayMetrics.density * 25f + 0.5f).toInt()
        }

    private fun log(msg: String) {
        Log.d("StatusBarColorCompat", msg)
    }

    fun setContentViewWithStatusBarColorByColorPrimaryDark(activity: Activity, layoutResID: Int) {
        setContentViewFitsSystemWindows(activity, layoutResID)
        setStatusBarColor(activity, getColorPrimaryDark(activity))
    }


    /**
     * 主要是在KITKAT下把根布局设置setFitsSystemWindows，
     * 来解决软键盘弹出的问题
     * 会再套一层FrameLayout，避免merge问题和
     * @param activity
     * @param layoutResID
     */
    fun setContentViewFitsSystemWindows(activity: Activity, layoutResID: Int) {
        if (IS_KITKAT) {
            // 要在根布局加setFitsSystemWindows，来解决软键盘的问题
            // 如果merge是根标签，必须指定ViewGroup root和attachToRoot=true
            // 否则android.view.InflateException: <merge /> can be used only with a
            // valid ViewGroup root and attachToRoot=true
            // 目前暂时未找到优雅的解决merge的方法
            // 情况一，只有一个child,就不是merge，直接把原来的View传给activity即可，
            // 但是如果把这个child设置了setFitsSystemWindows，其padding会重置为0
            // 情况二，count>1说明layout的根节点是merge，需要把这个套一层的contentContainer传给activity
            // 综上所述，在KITKAT下再套一个setFitsSystemWindows的FrameLayout即可
            val contentContainer = FrameLayout(activity)
            LayoutInflater.from(activity).inflate(layoutResID, contentContainer, true)
            // if(contentContainer.getChildCount() == 1){
            // View realContent = contentContainer.getChildAt(0);
            // realContent.setFitsSystemWindows(true);
            // contentContainer.removeViewAt(0);
            // activity.setContentView(realContent);
            // }else{
            contentContainer.fitsSystemWindows = true
            activity.setContentView(contentContainer)
            // }
        } else {
            activity.setContentView(layoutResID)
        }

    }

    /**
     * 设置状态栏颜色
     * 5.0以上用原生的window.setStatusBarColor
     * 4.4上用贴一个fakeStatusBarBackgroundView的方式
     * 其实5.0的window.setStatusBarColor也是把贴的statusBarBackgroundView设置颜色
     * @param activity
     * @param statusBarColor
     */
    @SuppressLint("NewApi")
    fun setStatusBarColor(activity: Activity, statusBarColor: Int) {
        if (IS_LOLLIPOP) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (window.statusBarColor != statusBarColor) {
                window.statusBarColor = statusBarColor
                log("LOLLIPOP window.getStatusBarColor() != statusBarColor, so update")
            } else {
                log("LOLLIPOP window.getStatusBarColor() == statusBarColor, so ingore")
            }
        } else if (IS_KITKAT) {
            val window = activity.window
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            // 在这里设置setSoftInputMode是没有效果的，要在super.onCreate之前设置才有效果
            // window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
            // | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            val decorView = window.decorView as ViewGroup
            var fakeStatusBarBackgroundView: View? = null
            // findViewWithTag会遍历所有的，我们在这里只遍历decorView的child即可
            val decorChildCount = decorView.childCount
            for (i in 0 until decorChildCount) {
                val child = decorView.getChildAt(i)
                val childTag = child.tag
                if (childTag != null) {
                    if (KITKAT_FAKE_STATUSBAR_BACKGROUND_VIEW_TAG == childTag) {
                        fakeStatusBarBackgroundView = child
                        log("find the fakeStatusBarBackgroundView by tag")
                        break
                    }
                }
            }
            if (fakeStatusBarBackgroundView == null) {
                fakeStatusBarBackgroundView = View(activity)
                log("NOT find the fakeStatusBarBackgroundView by tag, so new one")
                fakeStatusBarBackgroundView.tag = KITKAT_FAKE_STATUSBAR_BACKGROUND_VIEW_TAG
                decorView.addView(fakeStatusBarBackgroundView, ViewGroup.LayoutParams.MATCH_PARENT,
                        KITKAT_STATUSBAR_HEIGHT)
            }
            fakeStatusBarBackgroundView.setBackgroundColor(statusBarColor)
        }
    }

    /**
     * 取ColorPrimaryDark的值
     * 5.0以上优先去取android.R.attr.colorPrimaryDark
     * 其他情况取R.color.colorPrimaryDark（如果有的话）
     * @param activity
     * @return
     */
    @SuppressLint("InlinedApi")
    private fun getColorPrimaryDark(activity: Activity): Int {
        var statusBarColor = Color.TRANSPARENT
        if (IS_LOLLIPOP) {
            val a = activity.theme.obtainStyledAttributes(intArrayOf(android.R.attr.colorPrimaryDark))
            if (a.hasValue(0)) {
                statusBarColor = a.getColor(0, Color.TRANSPARENT)
            } else {
                statusBarColor = getColorPrimaryDarkFromResources(activity)
            }
            a.recycle()
        } else if (IS_KITKAT) {
            statusBarColor = getColorPrimaryDarkFromResources(activity)
        }
        return statusBarColor
    }

    private fun getColorPrimaryDarkFromResources(activity: Activity): Int {
        val res = activity.resources
        val colorPrimaryDarkResId = res.getIdentifier("colorPrimaryDark", "color", activity.packageName)
        return if (colorPrimaryDarkResId > 0) {
            res.getColor(colorPrimaryDarkResId)
        } else Color.TRANSPARENT
    }

    fun lightStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android6.0以后可以对状态栏文字颜色和图标进行修改
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

}
