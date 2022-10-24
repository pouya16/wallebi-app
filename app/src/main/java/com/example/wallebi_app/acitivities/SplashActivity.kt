package com.example.wallebi_app.acitivities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wallebi_app.R
import com.example.wallebi_app.database.UserLogin

class SplashActivity : AppCompatActivity() {

    lateinit var userLogin: UserLogin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        userLogin = UserLogin(this)


    }
}