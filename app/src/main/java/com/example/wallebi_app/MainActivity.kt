package com.example.wallebi_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wallebi_app.base.BaseActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}