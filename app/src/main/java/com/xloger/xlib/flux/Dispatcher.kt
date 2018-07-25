package com.xloger.xlib.flux

import com.xloger.xlib.tool.Xlog

/**
 * Created on 2018/7/24 10:11.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class Dispatcher {
    companion object {
        val instance: Dispatcher by lazy {
            Dispatcher()
        }
        private val storeList = mutableListOf<Store>()
    }

    fun register(store: Store) = storeList.add(store)

    fun unregister(store: Store) = storeList.remove(store)

    fun dispatch(action: Action<*>) = post(action)

    private fun post(action: Action<*>) {
        storeList.forEach {
            Xlog.debug("接收到Action： ${action.type}, ${action.data}")
            it.onAction(action)
        }
    }
}