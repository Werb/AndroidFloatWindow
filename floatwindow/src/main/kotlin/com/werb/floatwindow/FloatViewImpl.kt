package com.werb.floatwindow

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout

/**
 * Created by wanbo on 2018/8/1.
 */
internal class FloatViewImpl : FrameLayout, FloatView {

    /** float view */
    private lateinit var floatView: View
    private val floatLayoutParams: FrameLayout.LayoutParams by lazy { FrameLayout.LayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT) }
    private var gravity = Gravity.BOTTOM or Gravity.START
    private var floatPosition = FloatPosition.BOTTOM_START
    private var tag = FloatData.float_default_tag

    /** touch config */
    private var mX: Int = 0
    private var mY: Int = 0
    private var downX: Float = 0f
    private var downY: Float = 0f
    private var upX: Float = 0f
    private var upY: Float = 0f
    private var mClick = false
    private var mAnimator: ValueAnimator? = null
    private val mSlop: Int = ViewConfiguration.get(context.applicationContext).scaledTouchSlop
    private var moveBlock: ((String, Int, Int) -> Unit)? = null
    private var show = false

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun setFloatTag(tag: String) {
        this.tag = tag
    }

    override fun getFloatTag(): String {
        return tag
    }

    override fun setSize(width: Int, height: Int) {
        floatLayoutParams.width = width
        floatLayoutParams.height = height
    }

    override fun setView(view: View) {
        this.floatView = view
    }

    override fun setOffset(xOffset: Int, yOffset: Int) {
        updateOffset(xOffset, yOffset)
    }

    override fun setGravity(gravity: Int) {
        floatPosition = when (gravity) {
            Gravity.TOP or Gravity.START -> {
                this.gravity = gravity
                FloatPosition.TOP_START
            }
            Gravity.TOP or Gravity.END -> {
                this.gravity = gravity
                FloatPosition.TOP_END
            }
            Gravity.BOTTOM or Gravity.START -> {
                this.gravity = gravity
                FloatPosition.BOTTOM_START
            }
            Gravity.BOTTOM or Gravity.END -> {
                this.gravity = gravity
                FloatPosition.BOTTOM_END
            }
            else -> {
                FloatPosition.BOTTOM_START
            }
        }
    }

    override fun initUI() {
        // add view in parent
        addView(floatView, floatLayoutParams)
        floatLayoutParams.gravity = gravity

        floatView.visibility = View.GONE
        var lastX = 0f
        var lastY = 0f
        var changeX: Float
        var changeY: Float
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
                    lastX = event.rawX
                    lastY = event.rawY
                    when (floatPosition) {
                        FloatPosition.TOP_START -> {
                            updateXY((mX + changeX).toInt(), (mY + changeY).toInt())
                        }
                        FloatPosition.TOP_END -> {
                            updateXY((mX - changeX).toInt(), (mY + changeY).toInt())
                        }
                        FloatPosition.BOTTOM_START -> {
                            updateXY((mX + changeX).toInt(), (mY - changeY).toInt())
                        }
                        FloatPosition.BOTTOM_END -> {
                            updateXY((mX - changeX).toInt(), (mY - changeY).toInt())
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    upX = event.rawX
                    upY = event.rawY
                    mClick = (Math.abs(upX - downX) > mSlop) || (Math.abs(upY - downY) > mSlop)
                    // adsorb in left or right
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
        // sync position
        FloatWindow.floatXYmap[tag]?.apply {
            updateOffset(this.x, this.y)
            mX = this.x
            mY = this.y
        }
        floatView.visibility = View.VISIBLE
        show = true
    }

    override fun destroy() {
        removeView(floatView)
    }

    override fun updateXY(x: Int, y: Int) {
        mX = x
        // limit mY in parent height
        if (y in 0..this.height - floatView.height) {
            mY = y
        }
        updateOffset(mX, mY)
    }

    override fun updateX(x: Int) {
        mX = x
        updateOffset(mX, mY)
    }

    override fun updateY(y: Int) {
        mY = y
        updateOffset(mX, mY)
    }

    override fun getFloatViewX(): Int = mX

    override fun getFloatViewY(): Int = mY

    override fun addMoveListener(moveBlock: (String, Int, Int) -> Unit) {
        this.moveBlock = moveBlock
    }

    private fun updateOffset(xOffset: Int, yOffset: Int) {
        when (floatPosition) {
            FloatPosition.TOP_START -> {
                floatLayoutParams.topMargin = yOffset
                floatLayoutParams.leftMargin = xOffset
            }
            FloatPosition.TOP_END -> {
                floatLayoutParams.topMargin = yOffset
                floatLayoutParams.rightMargin = xOffset
            }
            FloatPosition.BOTTOM_START -> {
                floatLayoutParams.bottomMargin = yOffset
                floatLayoutParams.leftMargin = xOffset
            }
            FloatPosition.BOTTOM_END -> {
                floatLayoutParams.bottomMargin = yOffset
                floatLayoutParams.rightMargin = xOffset
            }
        }
        floatView.layoutParams = floatLayoutParams
        moveBlock?.invoke(tag, xOffset, yOffset)
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