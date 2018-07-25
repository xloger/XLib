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
    var dispatcher by Delegates.notNull<Dispatcher>()
    var actionCreator by Delegates.notNull<ActionCreator>()
    var store : Store? = null


    protected fun initDependencies(store: Store) {
        dispatcher = Dispatcher.instance
        actionCreator = ActionCreator.get(dispatcher)!!
        this.store = store
        dispatcher.register(store)
        store.register(this)
    }

    override fun onResume() {
        super.onResume()
        store?.register(this)
    }

    override fun onPause() {
        super.onPause()
        store?.unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        store?.let { dispatcher.unregister(it) }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onFluxStoreChange(event: Store.StoreChangeEvent) {
        onStoreChange(event)
    }

    abstract fun onStoreChange(event: Store.StoreChangeEvent)
}