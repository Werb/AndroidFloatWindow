package com.werb.androidfloatwindow

import android.os.Bundle
import com.werb.floatwindow.FloatWindow
import kotlinx.android.synthetic.main.activity_two.*

/**
 * Created by wanbo on 2018/7/31.
 */
class TwoActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two)

        button.setOnClickListener {
            FloatWindow.show(this)
        }

        button2.setOnClickListener {
            FloatWindow.add(this)
        }

    }

}