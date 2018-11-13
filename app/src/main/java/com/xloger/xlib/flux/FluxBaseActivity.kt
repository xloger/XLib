package com.xloger.xlib.flux

import com.xloger.xlib.tool.XActivity
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.properties.Delegates

/**
 * Created on 2018/7/24 16:00.
 * Author: xloger
 * Email:phoenix@xloger.com
 */
abstract class FluxBaseActivity : XActivity() {
    var dispatcher by Delegates.notNull<FluxDispatcher>()
//    var actionCreator by Delegates.notNull<FluxActionCreator>()
    var fluxStore : FluxStore? = null


    protected fun initDependencies(fluxStore: FluxStore) {
        dispatcher = FluxDispatcher.INSTANCE
//        actionCreator = FluxActionCreator.get(dispatcher)
        this.fluxStore = fluxStore
        dispatcher.register(fluxStore)
        fluxStore.register(this)
    }

    override fun onResume() {
        super.onResume()
        fluxStore?.register(this)
    }

    override fun onPause() {
        super.onPause()
        fluxStore?.unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        fluxStore?.let { dispatcher.unregister(it) }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFluxStoreChange(event: FluxStore.StoreChangeEvent) {
        onStoreChange(event)
    }

    abstract fun onStoreChange(event: FluxStore.StoreChangeEvent)
}