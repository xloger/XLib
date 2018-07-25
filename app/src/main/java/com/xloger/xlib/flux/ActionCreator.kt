package com.xloger.xlib.flux

/**
 * Created on 2018/7/24 10:10.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class ActionCreator(private val dispatcher: Dispatcher) {
    companion object {
        private var instance: ActionCreator? = null

        fun get(dispatcher: Dispatcher): ActionCreator? {
            if (instance == null) {
                instance = ActionCreator(dispatcher)
            }
            return instance
        }
    }
}