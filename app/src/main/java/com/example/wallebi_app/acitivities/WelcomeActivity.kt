package com.example.wallebi_app.acitivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wallebi_app.R
import com.google.android.material.button.MaterialButton

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val btnStart = findViewById<MaterialButton>(R.id.btn_start)

        btnStart.setOnClickListener { finish() }
    }
}