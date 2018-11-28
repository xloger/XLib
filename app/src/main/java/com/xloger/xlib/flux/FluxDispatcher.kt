package com.xloger.xlib.flux

import com.xloger.xlib.tool.Xlog

/**
 * Created on 2018/7/24 10:11.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class FluxDispatcher {
    companion object {
        val INSTANCE: FluxDispatcher by lazy {
            FluxDispatcher()
        }
        private val storeList = mutableListOf<FluxStore>()
    }

    fun register(fluxStore: FluxStore) = storeList.add(fluxStore)

    fun unregister(fluxStore: FluxStore) = storeList.remove(fluxStore)

    fun dispatch(action: FluxAction<*>) = post(action)

    private fun post(action: FluxAction<*>) {
        Xlog.debug("当前注册的Store: ${storeList.map { it.toString() + " " }}")
        storeList.forEach {
            Xlog.debug("接收到Action： ${action.type}, ${action.data}")
            it.onAction(action)
        }
    }
}