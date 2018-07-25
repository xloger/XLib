package com.xloger.xlib.flux

/**
 * Created on 2018/7/24 10:12.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
abstract class Store {
    fun register(view: Any) {

    }

    fun unregister(view: Any) {
        XEvent.register(view)
    }

    fun emitStoreChange(type: String = "") {
        XEvent.post(changeEvent(type))
    }

    abstract fun changeEvent(type: String = "") : StoreChangeEvent

    abstract fun onAction(action: Action<*>)

    open class StoreChangeEvent(val type: String)
}