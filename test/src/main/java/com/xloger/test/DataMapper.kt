package com.xloger.test

import com.xloger.test.data.Forecast
import com.xloger.test.data.ForecastResult
import com.xloger.test.domain.ForecastList
import java.text.DateFormat
import java.util.*
import com.xloger.test.domain.Forecast as ModelForecast

/**
 * Created on 2017/10/11 15:38.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */
public class ForecastDataMapper {
    fun convertFromDataModel(forecast: ForecastResult): ForecastList =
            ForecastList(forecast.city.name, forecast.city.country, convertForecastListToDomain(forecast.list))
    private fun convertForecastListToDomain(list: List<Forecast>): List<ModelForecast> =
            list.map { convertForecastItemToDomain(it) }
    private fun convertForecastItemToDomain(forecast: Forecast): ModelForecast =
            ModelForecast(convertDate(forecast.dt), forecast.weather[0].description, forecast.temp.max.toInt(), forecast.temp.min.toInt(),
                    generateIconUrl(forecast.weather[0].icon))
    private fun convertDate(date: Long): String {
        val df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault())
        return df.format(date * 1000)
    }

    private fun generateIconUrl(iconCode: String): String
            = "http://openweathermap.org/img/w/$iconCode.png"
}