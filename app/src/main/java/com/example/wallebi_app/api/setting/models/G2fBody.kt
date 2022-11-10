package com.example.wallebi_app.api.setting.models

data class G2fBody(
    val email:String,
    val g2f:String,
    val type:String,
    val otp:String?
)
