package com.xloger.xlib.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.xloger.xlib.R

/**
 * Created on 2019/2/23 19:06.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
abstract class XBaseDialog(val ctx: Context) : Dialog(ctx, R.style.XBaseDialog) {
    protected open var gravity = Gravity.CENTER

    protected lateinit var mRootView: View

    protected abstract fun getLayoutId(): Int

    protected abstract fun afterCreate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mRootView = View.inflate(ctx, getLayoutId(), null)
        setContentView(mRootView)
        window?.let {
            val lp = it.attributes
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT
            lp.gravity = gravity
            it.attributes = lp

            it.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

        afterCreate()
    }


}