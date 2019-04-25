package com.xloger.xlib.tool

import android.content.Intent
import android.net.Uri
import org.jetbrains.anko.commons.BuildConfig

/**
 * Created on 2019/4/25 15:20.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
object XMap {

    fun navigation(addressName: String,lat: String, lng : String) {
        if (XNative.isInstallApp(XInit.applicationContext!!, "com.autonavi.minimap")) {
            navigationByAmap(addressName, lat, lng)
        } else if (XNative.isInstallApp(XInit.applicationContext!!, "com.baidu.BaiduMap")) {
            navigationByBaidu(addressName)
        } else {
            Xlog.toast("请安装高德地图或者百度地图")
        }
    }

    fun navigationByBaidu(addressName: String, mode: String = "driving") {
        val intentString = "baidumap://map/direction?destination=${addressName}&coord_type=gcj02&mode=${mode}&src=${XTool.getSelfPacakgeName(XInit.applicationContext!!)}"
        try {
            val intent = Intent()
            intent.setData(Uri.parse(intentString))
            XInit.applicationContext?.startActivity(intent)
        } catch (ex: Exception) {
            Xlog.toast("您没有安装百度地图")

        }
    }

    fun navigationByAmap(addressName: String,lat: String, lng : String, mode: String = "driving") {
        val intentString = "amapuri://route/plan/?dname=${addressName}&dlat=${lat}&lng=${lng}&dev=0&coord_type=gcj02&t=0&sourceApplication=${XTool.getSelfPacakgeName(XInit.applicationContext!!)}"
        try {
            val intent = Intent()
            intent.setData(Uri.parse(intentString))
            XInit.applicationContext?.startActivity(intent)
        } catch (ex: Exception) {
            Xlog.toast("您没有安装高德地图")

        }
    }

}