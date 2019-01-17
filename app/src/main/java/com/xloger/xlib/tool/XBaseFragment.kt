package com.xloger.xlib.tool

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created on 2018/7/24 16:50.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
abstract class XBaseFragment : Fragment() {
    protected lateinit var mRootView: View

    protected abstract fun getLayoutId() : Int

    protected abstract fun afterCreate(savedInstanceState: Bundle?)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!::mRootView.isInitialized) {
            mRootView = inflater.inflate(getLayoutId(), container, false)
        }
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        afterCreate(savedInstanceState)
    }
}