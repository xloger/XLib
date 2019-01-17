package com.xloger.xlib.flux

/**
 * Created on 2018/7/24 10:12.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
abstract class FluxStore {
    fun register(view: Any) {
        XEvent.register(view)
    }

    fun unregister(view: Any) {
        XEvent.unregister(view)
    }

    fun emitStoreChange(type: String = "") {
        XEvent.post(changeEvent(type))
    }

    abstract fun changeEvent(type: String = "", vararg any: Any) : StoreChangeEvent

    abstract fun onAction(action: FluxAction<*>)

    open class StoreChangeEvent(val type: String)
}