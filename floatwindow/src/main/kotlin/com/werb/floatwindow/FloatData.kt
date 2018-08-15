package com.werb.floatwindow

import android.view.View

/**
 * Created by wanbo on 2018/8/1.
 */
internal data class FloatData(var view: View? = null,
                     var width: Int = 0,
                     var height: Int = 0,
                     var xOffset: Int = 0,
                     var yOffset: Int = 0,
                     var moveListener: ((Int, Int) -> Unit)? = null,
                     var gravity: Int? = null,
                     var tag: String = float_default_tag) {

    companion object {
        const val float_default_tag = "float_window_tag"
    }

}

internal data class FloatXY(var x: Int, var y: Int)

enum class FloatPosition { TOP_START, TOP_END, BOTTOM_START, BOTTOM_END }