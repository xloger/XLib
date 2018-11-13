package com.xloger.xlib.flux

/**
 * Created on 2018/7/24 10:10.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
open class FluxActionCreator(private val fluxDispatcher: FluxDispatcher) {
    companion object {
        private lateinit var instance: FluxActionCreator

        fun get(fluxDispatcher: FluxDispatcher): FluxActionCreator {
            if (!this::instance.isInitialized) {
                instance = FluxActionCreator(fluxDispatcher)
            }
            return instance
        }
    }
}