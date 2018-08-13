package com.werb.floatwindow

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View


/**
 * Created by wanbo on 2018/8/1.
 */
object FloatWindow {

    private val floatWindowMap: MutableMap<String, FloatViewImpl> by lazy { mutableMapOf<String, FloatViewImpl>() }

    private fun get(tag: String = FloatData.float_default_tag): FloatViewImpl? {
        return floatWindowMap[tag]
    }

    fun show(tag: String = FloatData.float_default_tag) {
        get(tag)?.show()
    }

    fun dismiss(tag: String = FloatData.float_default_tag) {
        get(tag)?.dismiss()
    }

    fun destroy(tag: String = FloatData.float_default_tag) {
        if (floatWindowMap.containsKey(tag)) {
            floatWindowMap[tag]?.destroy()
            floatWindowMap.remove(tag)
        }
    }


    class Builder(private val context: Context) {

        private val floatData: FloatData = FloatData()

        fun setView(view: View): Builder {
            floatData.view = view
            return this
        }

        fun setView(layoutId: Int): Builder {
            floatData.view = LayoutInflater.from(context).inflate(layoutId, null)
            return this
        }

        fun setSize(width: Int, height: Int): Builder {
            floatData.width = width
            floatData.height = height
            return this
        }

        fun setOffset(xOffset: Int, yOffset: Int): Builder {
            floatData.xOffset = xOffset
            floatData.yOffset = yOffset
            return this
        }

        fun setGravity(gravity: Int): Builder {
            floatData.gravity = gravity
            return this
        }

        fun setTag(tag: String): Builder {
            floatData.tag = tag
            return this
        }

        fun build(): View {
            if (floatData.view == null) {
                throw IllegalArgumentException("View has not been set!")
            }
            if (floatWindowMap.containsKey(floatData.tag)) {
                throw IllegalArgumentException("FloatWindow of this tag has been added, Please set a new tag for the new FloatWindow")
            }
            val floatView = FloatViewImpl(context).apply {
                this.setView(floatData.view ?: return@apply)
                this.setSize(floatData.width, floatData.height)
                this.setGravity(floatData.gravity ?: Gravity.BOTTOM or Gravity.START)
                this.setOffset(floatData.xOffset, floatData.yOffset)
            }
            floatWindowMap[floatData.tag] = floatView
            return floatView
        }

    }

}