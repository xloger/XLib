package com.xloger.xlib.flux

/**
 * Created on 2018/7/24 10:10.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
open class FluxActionCreator(private val fluxDispatcher: FluxDispatcher) {
    companion object {
        private var instance: FluxActionCreator? = null

        fun get(fluxDispatcher: FluxDispatcher): FluxActionCreator {
            if (instance == null) {
                instance = FluxActionCreator(fluxDispatcher)
            }
            return instance!!
        }
    }
}