package com.xloger.test.activity

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xloger.test.R
import com.xloger.xlib.tool.XPermission
import com.xloger.xlib.tool.Xlog
import kotlinx.android.synthetic.main.activity_test.*
import org.jetbrains.anko.act
import org.jetbrains.anko.ctx

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
//        check.setOnClickListener{
//            with(Build.VERSION_CODES.M) {
//                val permissions = XPermission.findDeniedPermissions(ctx, XPermission.Write_SD)
//                if (permissions.size == 0) {
//                    Xlog.toast(ctx,"有SD卡权限，保存在公开目录")
//                } else {
//                    Xlog.toast(ctx,"没有权限")
//                }
//                return@setOnClickListener
//            }
//            Xlog.toast(ctx,"不需要运行时权限")
//        }
//        checkOld.setOnClickListener{
//            with(Build.VERSION_CODES.M) {
//                val permissions = XPermission.findDeniedPermissionsOld(ctx, XPermission.Write_SD)
//                if (permissions.size == 0) {
//                    Xlog.toast(ctx,"有SD卡权限，保存在公开目录")
//                } else {
//                    Xlog.toast(ctx,"没有权限")
//                }
//                return@setOnClickListener
//            }
//            Xlog.toast(ctx,"不需要运行时权限")
//        }
        check.setOnClickListener {
            XPermission.requestPermission(act, object : XPermission.XPermissionCallback {
                override fun onSuccess() {
                    Xlog.log("有权限了")
                }

                override fun onRefuse(deniedPermissions: List<String>) {
                    Xlog.log("没有权限了")
                }
            }, XPermission.Camera)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        XPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}
