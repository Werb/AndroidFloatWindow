package com.werb.androidfloatwindow

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.werb.floatwindow.FloatWindow
import com.werb.floatwindow.addFloatWindow


/**
 * Created by wanbo on 2018/7/31.
 */
@SuppressLint("Registered")
open class BaseActivity: AppCompatActivity() {

    private var floatView_A: View? = null
    private val floatView_2: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        floatView_A = FloatWindow.Builder(this.applicationContext)
            .setView(ImageView(this).apply {
                setImageResource(R.mipmap.ic_launcher)
            })
            .setSize(100, 100)
            .setOffset(20, 20)
            .build()
    }

    override fun onPostResume() {
        super.onPostResume()
        FloatWindow.show()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        addFloatWindow(floatView_A ?: return)
    }


}