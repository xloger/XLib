package com.xloger.test.domain

/**
 * Created on 2017/10/11 15:30.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */
public interface Command<T> {
    fun execute(): T
}

data class ForecastList(val city: String, val country: String,
                        val dailyForecast:List<Forecast>) {
    operator fun get(position: Int): Forecast = dailyForecast[position]
    fun size(): Int = dailyForecast.size
}
data class Forecast(val date: String, val description: String, val high: Int,
                    val low: Int, val iconUrl: String)