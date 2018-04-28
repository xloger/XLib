package com.xloger.test.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xloger.test.R
import com.xloger.xlib.tool.Xlog
import kotlinx.android.synthetic.main.activity_toast_test.*
import org.jetbrains.anko.ctx
import org.jetbrains.anko.doAsync

class ToastTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toast_test)

        var i = 0
        toast_test.setOnClickListener {
            Xlog.toast(ctx, "弹消息$i")
            i += 1
        }

        var j = 0
        toast_app_test.setOnClickListener {
            Xlog.toast("弹消息$j")
            j += 1
        }
        var k = 0
        toast_thread_test.setOnClickListener {
            doAsync {
                Xlog.toast(ctx, "弹消息$k")
                k += 1
            }
        }
    }
}
