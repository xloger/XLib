package com.xloger.xlib.tool

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Created on 2019/4/25 15:20.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
object XMap {

    fun navigation(context: Context, addressName: String,lat: String, lng : String) {
        if (XNative.isInstallApp(context, "com.autonavi.minimap")) {
            navigationByAmap(context, addressName, lat, lng)
        } else if (XNative.isInstallApp(context, "com.baidu.BaiduMap")) {
            navigationByBaidu(context, addressName)
        } else {
            Xlog.toast("请安装高德地图或者百度地图")
        }
    }

    fun navigationByBaidu(context: Context, addressName: String, mode: String = "driving") {
        val intentString = "baidumap://map/direction?destination=${addressName}&coord_type=gcj02&mode=${mode}&src=${XTool.getSelfPackageName(context)}"
        try {
            val intent = Intent()
            intent.setData(Uri.parse(intentString))
            context.startActivity(intent)
        } catch (ex: Exception) {
            Xlog.toast("您没有安装百度地图")

        }
    }

    fun navigationByAmap(context: Context, addressName: String,lat: String, lng : String, mode: String = "driving") {
        val intentString = "amapuri://route/plan/?dname=${addressName}&dlat=${lat}&lng=${lng}&dev=0&coord_type=gcj02&t=0&sourceApplication=${XTool.getSelfPackageName(context)}"
        try {
            val intent = Intent()
            intent.setData(Uri.parse(intentString))
            context.startActivity(intent)
        } catch (ex: Exception) {
            Xlog.toast("您没有安装高德地图")

        }
    }

}