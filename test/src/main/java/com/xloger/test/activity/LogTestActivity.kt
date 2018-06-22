package com.xloger.test.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.xloger.test.R
import com.xloger.xlib.tool.Xlog
import kotlinx.android.synthetic.main.activity_log_test.*

class LogTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_test)
        log_long_length.setOnClickListener {
            var log: StringBuffer = StringBuffer()
            for (i in 1..10000) {
                log.append(i.toString() + " ")
            }
            Xlog.e(log.toString())
        }
    }
}
