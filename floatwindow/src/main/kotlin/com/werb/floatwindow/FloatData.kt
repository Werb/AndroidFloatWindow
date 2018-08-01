package com.werb.floatwindow

import android.view.View

/**
 * Created by wanbo on 2018/8/1.
 */
data class FloatData(var view: View? = null,
                     var width: Int? = null,
                     var height: Int? = null,
                     var xOffset: Int? = null,
                     var yOffset: Int? = null,
                     var moveListener: ((Int, Int) -> Unit)? = null,
                     var tag: String = float_default_tag) {

    companion object {
        const val float_default_tag = "float_window_tag"
    }

}