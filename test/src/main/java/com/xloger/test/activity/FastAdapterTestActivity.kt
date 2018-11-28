package com.xloger.test.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.xloger.test.R
import com.xloger.test.domain.Forecast
import com.xloger.test.tool.ctx
import com.xloger.xlib.tool.FastAdapter
import com.xloger.xlib.tool.Xlog
import kotlinx.android.synthetic.main.activity_fast_adapter_test.*
import kotlinx.android.synthetic.main.item_forecast.view.*

class FastAdapterTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fast_adapter_test)
        val forecastList = mutableListOf<Forecast>()
        forecastList.add(Forecast("2018-1-1", "多云转晴", 32, 24, "图片地址"))
        forecastList.add(Forecast("2018-1-2", "多云转晴", 34, 14, "图片地址"))
        forecastList.add(Forecast("2018-1-3", "多云转晴", 23, 13, "图片地址"))
        with(fast_test_recycler_view) {
            adapter = FastAdapter(forecastList, R.layout.item_forecast) { holder: FastAdapter.ViewHolder, bean: Forecast, _ ->
                with(holder.itemView) {
                    date.text = bean.date
                    description.text = bean.description
                    maxTemperature.text = bean.high.toString()
                    minTemperature.text = bean.low.toString()
                    setOnClickListener {
                        Xlog.toast("被点击了")
                    }
                }
            }
            layoutManager = LinearLayoutManager(ctx)
        }
    }
}
