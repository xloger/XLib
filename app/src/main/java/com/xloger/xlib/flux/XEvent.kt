package com.xloger.xlib.flux

import android.view.View
import org.greenrobot.eventbus.EventBus

/**
 * Created on 2018/7/24 11:20.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
class XEvent {

    companion object {
        val event = EventBus.getDefault()

        fun register(any: Any) {
            if (!event.isRegistered(any)){
                event.register(any)
            }
        }

        fun post(any: Any) {
            event.post(any)
        }

        fun postSticky(any: Any) {
            event.postSticky(any)
        }
    }


}