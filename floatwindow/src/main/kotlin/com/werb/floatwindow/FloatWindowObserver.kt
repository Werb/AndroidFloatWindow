package com.werb.floatwindow

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.view.View

/**
 * Created by wanbo on 2018/8/13.
 */
class FloatWindowObserver(private val activity: Activity,
                          private vararg val views: View,
                          private val showBlock: (tag: String) -> Unit = {},
                          private val dismissBlock: (tag: String) -> Unit = {}) : LifecycleObserver {

    init {
        val set = HashSet<String>()
        views.filterIsInstance<FloatViewImpl>().forEach {
            val key = it.getFloatTag()
            if (!set.add(key)) {
                if (key == FloatData.float_default_tag) {
                    throw IllegalArgumentException("FloatWindow of default tag $key has been added, Please set a new tag for the new FloatWindow")
                } else {
                    throw IllegalArgumentException("FloatWindow of this tag $key has been added, Please set a new tag for the new FloatWindow")
                }
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun add() {
        views.forEach {
            activity.addFloatWindow(it)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun show() {
        views.forEach { view ->
            if (view is FloatViewImpl) {
                FloatWindow.float_show_map[view.getFloatTag()]?.let {
                    if (it.show) {
                        showBlock(view.getFloatTag())
                        view.show()
                    } else {
                        if (view.hasAddFloatView()) {
                            dismissBlock(view.getFloatTag())
                            view.dismiss()
                            if (it.destroy) {
                                view.destroy()
                            }
                        }
                    }
                }
            }
        }
    }

}