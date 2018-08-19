package com.werb.androidfloatwindow

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.Toast
import kotlinx.android.synthetic.main.layout_rotate_view.view.*


/**
 * Created by wanbo on 2018/8/19.
 */
class RotateView: FrameLayout {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initUI()
    }

    private fun initUI(){
        LayoutInflater.from(context).inflate(R.layout.layout_rotate_view, this)
        jay.apply {
            this.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate).also {
                it.interpolator = LinearInterpolator()
            })
        }
    }

}