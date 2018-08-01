package com.werb.floatwindow

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout

/**
 * Created by wanbo on 2018/8/1.
 */
internal class FloatViewImpl : FrameLayout, FloatView {

    private lateinit var floatView: View
    private val floatLayoutParams: MarginLayoutParams by lazy { MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.MATCH_PARENT) }
    private var mX: Int = 0
    private var mY: Int = 0
    private var downX: Float = 0f
    private var downY: Float = 0f
    private var upX: Float = 0f
    private var upY: Float = 0f
    private var mClick = false
    private var mAnimator: ValueAnimator? = null
    private val mSlop: Int = ViewConfiguration.get(context.applicationContext).scaledTouchSlop
    private var moveBlock: ((Int, Int) -> Unit)? = null
    private var show = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setSize(width: Int, height: Int) {
        floatLayoutParams.width = width
        floatLayoutParams.height = height
    }

    override fun setView(view: View) {
        this.floatView = view
    }

    override fun setOffset(xOffset: Int, yOffset: Int) {
        floatLayoutParams.leftMargin = xOffset
        floatLayoutParams.bottomMargin = yOffset
    }

    override fun initUI() {
        addView(floatView, floatLayoutParams)
        floatView.visibility = View.GONE
        var lastX = 0f
        var lastY = 0f
        var changeX: Float
        var changeY: Float
        var newX: Float
        var newY: Float
        floatView.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    downX = event.rawX
                    downY = event.rawY
                    lastX = event.rawX
                    lastY = event.rawY
                    cancelAnimator()
                }
                MotionEvent.ACTION_MOVE -> {
                    changeX = event.rawX - lastX
                    changeY = event.rawY - lastY
                    newX = mX + changeX
                    newY = mY - changeY
                    lastX = event.rawX
                    lastY = event.rawY
                    updateXY(newX.toInt(), newY.toInt())
                }
                MotionEvent.ACTION_UP -> {
                    upX = event.rawX
                    upY = event.rawY
                    mClick = (Math.abs(upX - downX) > mSlop) || (Math.abs(upY - downY) > mSlop)
                    val startX = mX
                    val endX = if (startX * 2 + view.width > context.widthPixels)
                        context.widthPixels - view.width
                    else
                        0
                    mAnimator = ObjectAnimator.ofInt(startX, endX)
                    mAnimator?.addUpdateListener { animation ->
                        val x = animation.animatedValue as Int
                        updateX(x)
                    }
                    startAnimator()
                }
            }

            return@setOnTouchListener true
        }
    }

    override fun dismiss() {
        floatView.visibility = View.GONE
        show = false
    }

    override fun show() {
        if (childCount == 0) {
            initUI()
        }
        floatView.visibility = View.VISIBLE
        show = true
    }

    override fun destory() {
        removeView(floatView)
    }

    override fun updateXY(x: Int, y: Int) {
        mX = x
        mY = y
        floatLayoutParams.leftMargin = mX
        floatLayoutParams.bottomMargin = mY
        floatView.layoutParams = floatLayoutParams
        moveBlock?.invoke(mX, mY)
    }

    override fun updateX(x: Int) {
        mX = x
        floatLayoutParams.leftMargin = mX
        floatLayoutParams.bottomMargin = mY
        floatView.layoutParams = floatLayoutParams
        moveBlock?.invoke(mX, mY)
    }

    override fun updateY(y: Int) {
        mY = y
        floatLayoutParams.leftMargin = mX
        floatLayoutParams.bottomMargin = mY
        floatView.layoutParams = floatLayoutParams
        moveBlock?.invoke(mX, mY)
    }

    override fun getFloatViewX(): Int = mX

    override fun getFloatViewY(): Int = mY

    override fun addMoveListener(moveBlock: (Int, Int) -> Unit) {
        this.moveBlock = moveBlock
    }

    private fun startAnimator() {
        mAnimator?.let {
            it.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    it.removeAllUpdateListeners()
                    it.removeAllListeners()
                    mAnimator = null
                }
            })
            it.setDuration(200).start()
        }
    }

    private fun cancelAnimator() {
        mAnimator?.let {
            if (it.isRunning) {
                it.cancel()
            }
        }
    }
}