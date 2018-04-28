package com.xloger.test

import com.google.gson.Gson
import com.xloger.test.data.ForecastResult
import java.net.URL

/**
 * Created on 2017/10/11 11:44.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */
public class ForecastRequest(val zipCode: String) {
    companion object {
        private val APP_ID = "15646a06818f61f7b8d7823ca833e1ce"
        private val u = "http://api.openweathermap.org/data/2.5/" +
                "forecast/daily?mode=json&units=metric&cnt=7"
        private val COMPLETE_URL: String = "$u&APPID=$APP_ID&q="
    }

    fun execute(): ForecastResult {
        val forecastJsonStr = URL(COMPLETE_URL + zipCode).readText()
        return Gson().fromJson(forecastJsonStr, ForecastResult::class.java)
    }

}