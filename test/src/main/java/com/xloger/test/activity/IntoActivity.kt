package com.xloger.test.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xloger.test.R
import kotlinx.android.synthetic.main.activity_into.*
import org.jetbrains.anko.startActivity

class IntoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_into)

        into_permission.setOnClickListener {
            startActivity<XPermissionTestActivity>()
        }
        into_toast.setOnClickListener {
            startActivity<ToastTestActivity>()
        }
        into_log.setOnClickListener {
            startActivity<LogTestActivity>()
        }
    }
}
