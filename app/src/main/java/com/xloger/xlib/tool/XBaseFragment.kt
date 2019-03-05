package com.xloger.xlib.tool

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.Exception

/**
 * Created on 2018/7/24 16:50.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
abstract class XBaseFragment : Fragment() {
    protected lateinit var mRootView: View

    protected abstract fun getLayoutId(): Int

    protected open fun dslView(): View? {
        return null
    }

    protected abstract fun afterCreate(savedInstanceState: Bundle?)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!::mRootView.isInitialized) {
            if (getLayoutId() != 0) {
                mRootView = inflater.inflate(getLayoutId(), container, false)
            } else {
                dslView()?.notNull {
                    mRootView = it
                } ?: throw Exception("无法渲染 Fragment")
            }

        }
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        afterCreate(savedInstanceState)
    }
}