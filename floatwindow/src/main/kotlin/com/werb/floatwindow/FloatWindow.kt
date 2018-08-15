package com.werb.floatwindow

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View


/**
 * Created by wanbo on 2018/8/1.
 */
object FloatWindow {

    internal val floatXYmap = mutableMapOf<String, FloatXY>()

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

        fun build(): View {
            if (floatData.view == null) {
                throw IllegalArgumentException("View has not been set!")
            }

            if (!floatXYmap.containsKey(floatData.tag)) {
                floatXYmap[floatData.tag] = FloatXY(0, 0)
            }

            return FloatViewImpl(context).apply {
                this.setFloatTag(floatData.tag)
                this.setView(floatData.view ?: return@apply)
                this.setSize(floatData.width, floatData.height)
                this.setGravity(floatData.gravity ?: Gravity.BOTTOM or Gravity.START)
                this.setOffset(floatData.xOffset, floatData.yOffset)
                this.addMoveListener { tag, x, y ->
                    floatXYmap[tag]?.apply {
                        this.x = x
                        this.y = y
                        floatData.moveListener?.invoke(x, y)
                    }
                }
            }
        }

    }

}