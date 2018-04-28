package com.xloger.test.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.xloger.test.R
import com.xloger.test.adapter.ForecastListAdapter
import com.xloger.test.domain.Forecast
import com.xloger.test.domain.RequstForecastCommand

import com.xloger.xlib.tool.XPermission
import com.xloger.xlib.tool.Xlog
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private val items = listOf<String>(
            "Mon 6/23 - Sunny - 31/17",
            "Tue 6/24 - Foggy - 21/8",
            "Wed 6/25 - Cloudy - 22/17",
            "Thurs 6/26 - Rainy - 18/11",
            "Fri 6/27 - Foggy - 21/10",
            "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
            "Sun 6/29 - Sunny - 20/7"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        forecast_list.layoutManager = LinearLayoutManager(this)
        async {
            val result = RequstForecastCommand("94043").execute()
            uiThread {
                forecast_list.adapter = ForecastListAdapter(result, { forecast -> toast(forecast.date) })
            }
        }
        XPermission.requestPermission(this, object : XPermission.XPermissionCallback {
            override fun onSuccess() {
                Xlog.log("success")
                toast("gei le")
            }

            override fun onRefuse(deniedPermissions: List<String>) {
                Xlog.log("refuse")
                toast("Ju jue le")
            }
        }, XPermission.Write_SD)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        XPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }
}



