package com.xloger.test.domain

import com.xloger.test.ForecastDataMapper
import com.xloger.test.ForecastRequest

/**
 * Created on 2017/10/11 16:30.
 * Editor:xloger
 * Email:phoenix@xloger.com
 */
class RequstForecastCommand(private val zipCode: String) : Command<ForecastList> {
    override fun execute(): ForecastList {
        val forecastRequest = ForecastRequest(zipCode)
        return ForecastDataMapper().convertFromDataModel(forecastRequest.execute())
    }
}