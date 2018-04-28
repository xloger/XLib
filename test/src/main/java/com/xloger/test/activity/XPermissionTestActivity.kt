package com.xloger.test.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xloger.test.R
import com.xloger.xlib.tool.XPermission
import com.xloger.xlib.tool.Xlog
import kotlinx.android.synthetic.main.activity_xpermission_test.*
import org.jetbrains.anko.act
import org.jetbrains.anko.ctx
import org.jetbrains.anko.toast

class XPermissionTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xpermission_test)

        permission_test.setOnClickListener {
            XPermission.requestPermission(this, object : XPermission.XPermissionCallback {
                override fun onSuccess() {
                    toast("申请成功")
                }

                override fun onRefuse(deniedPermissions: List<String>) {
                    XPermission.defaultFailToast(ctx, deniedPermissions)
                }
            }, XPermission.Write_SD, XPermission.Camera)
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        XPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, act)
    }
}
