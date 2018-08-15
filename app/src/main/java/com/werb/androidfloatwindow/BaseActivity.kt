package com.werb.androidfloatwindow

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.werb.floatwindow.*


/**
 * Created by wanbo on 2018/7/31.
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private val floatView: View by lazy {
        FloatWindow.Builder(this.applicationContext)
            .setView(ImageView(this).apply {
                setImageResource(R.mipmap.ic_launcher)
            })
            .setSize(200, 200)
            .setOffset(0, 0)
            .setGravity(Gravity.BOTTOM or Gravity.END)
            .setMoveListener { x, y ->
                println("floatView1:$x----$y")
            }
            .build()
    }

    private val floatView2: View by lazy {
        FloatWindow.Builder(this.applicationContext)
            .setView(ImageView(this).apply {
                setImageResource(R.mipmap.ic_launcher)
            })
            .setSize(200, 200)
            .setOffset(0, 0)
            .setGravity(Gravity.TOP or Gravity.START)
            .setTag("111")
            .setMoveListener { x, y ->
                println("floatView2:$x----$y")
            }
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(FloatWindowObserver(this, floatView, floatView2))
    }

}