package com.werb.androidfloatwindow

import android.content.Intent
import android.os.Bundle
import com.werb.floatwindow.FloatWindow
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            startActivity(Intent(this, TwoActivity::class.java))
        }
        button2.setOnClickListener {
            FloatWindow.dismiss(this)
        }
        button3.setOnClickListener {
            FloatWindow.destroy(this)
        }
    }

}
