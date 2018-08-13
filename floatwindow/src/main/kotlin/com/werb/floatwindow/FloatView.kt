package com.werb.floatwindow

import android.view.View

/**
 * Float window view
 * Created by wanbo on 2018/8/1.
 */

interface FloatView {

    fun setTag(tag: String)

    fun setSize(width: Int, height: Int)

    fun setView(view: View)

    fun setOffset(xOffset: Int, yOffset: Int)

    fun setGravity(gravity: Int)

    fun initUI()

    fun dismiss()

    fun show()

    fun destroy()

    fun updateXY(x: Int, y: Int)

    fun updateX(x: Int)

    fun updateY(y: Int)

    fun getFloatViewX(): Int

    fun getFloatViewY(): Int

    fun addMoveListener(moveBlock: (Int, Int) -> Unit)

}