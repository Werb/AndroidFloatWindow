package com.werb.floatwindow

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import java.lang.ref.WeakReference


/**
 * Created by wanbo on 2018/8/1.
 */
object FloatWindow {

    internal val float_xy_map = mutableMapOf<String, FloatXY>()
    internal val float_show_map = mutableMapOf<String, Boolean>()

    fun show(activity: Activity, tag: String = FloatData.float_default_tag) {
        val act = WeakReference<Activity>(activity).get()
        act?.apply {
            val floatViewImpl = this.findViewById(tag.hashCode()) as? FloatViewImpl
            floatViewImpl?.show()
            float_show_map[tag] = true
        }
    }

    fun dismiss(activity: Activity, tag: String = FloatData.float_default_tag) {
        val act = WeakReference<Activity>(activity).get()
        act?.apply {
            val floatViewImpl = this.findViewById(tag.hashCode()) as? FloatViewImpl
            floatViewImpl?.dismiss()
            float_show_map[tag] = false
        }
    }

    fun destroy(activity: Activity, tag: String = FloatData.float_default_tag) {
        val act = WeakReference<Activity>(activity).get()
        act?.apply {
            val floatViewImpl = this.findViewById(tag.hashCode()) as? FloatViewImpl
            floatViewImpl?.dismiss()
            floatViewImpl?.destroy()
            float_show_map[tag] = false
            float_xy_map[tag] = FloatXY(0,0)
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

        fun setMoveListener(moveListener: ((x: Int, y: Int) -> Unit)): Builder {
            floatData.moveListener = moveListener
            return this
        }

        fun setAutoShow(autoShow: Boolean): Builder {
            floatData.autoShow = autoShow
            return this
        }

        fun build(): View {
            if (floatData.view == null) {
                throw IllegalArgumentException("View has not been set!")
            }

            if (!float_xy_map.containsKey(floatData.tag)) {
                float_xy_map[floatData.tag] = FloatXY(0, 0)
                float_show_map[floatData.tag] = floatData.autoShow
            }

            return FloatViewImpl(context).apply {
                this.setFloatTag(floatData.tag)
                this.setView(floatData.view ?: return@apply)
                this.setSize(floatData.width, floatData.height)
                this.setGravity(floatData.gravity ?: Gravity.BOTTOM or Gravity.START)
                this.setOffset(floatData.xOffset, floatData.yOffset)
                this.addMoveListener { tag, x, y ->
                    float_xy_map[tag]?.apply {
                        this.x = x
                        this.y = y
                        floatData.moveListener?.invoke(x, y)
                    }
                }
            }
        }

    }

}