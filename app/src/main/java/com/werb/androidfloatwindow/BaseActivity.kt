package com.werb.androidfloatwindow

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.werb.floatwindow.*


/**
 * Created by wanbo on 2018/7/31.
 */
@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private val rotateView: RotateView by lazy { RotateView(this) }

    private val floatView: View by lazy {
        FloatWindow.Builder(this)
            .setView(rotateView)
            .setSize(200, 200)
            .setOffset(0, 0)
            .setGravity(Gravity.BOTTOM or Gravity.END)
            .setMoveListener { x, y ->
                println("floatView1:$x----$y")
            }
            .build()
    }

    private val floatView2: View by lazy {
        FloatWindow.Builder(this)
            .setView(ImageView(this).apply {
                setImageResource(R.mipmap.ic_launcher)
                setOnClickListener {
                    Toast.makeText(this@BaseActivity, "image click", Toast.LENGTH_SHORT).show()
                }
            })
            .setSize(200, 200)
            .setOffset(0, 0)
            .setGravity(Gravity.TOP or Gravity.START)
            .setTag("floatView2")
            .setAutoShow(true)
            .setMoveListener { x, y ->
                println("floatView2:$x----$y")
            }
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(FloatWindowObserver(this, floatView, floatView2, showBlock = this::floatShow, dismissBlock = this::floatDismiss))
    }

    /** when floatView is show when Activity ON_RESUME we can do something */
    private fun floatShow(tag: String) {
        when(tag) {
            "floatView2" -> {
                println("------- floatView2 is show --------")
            }
            else -> {
                println("------- floatView1 is show --------")
            }
        }
    }

    /** when floatView is dismiss when Activity ON_RESUME we can do something */
    private fun floatDismiss(tag: String) {
        when(tag) {
            "floatView2" -> {
                println("------- floatView2 is dismiss now --------")
            }
            else -> {
                println("------- floatView1 is dismiss now --------")
            }
        }
    }

}