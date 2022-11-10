package com.example.wallebi_app.api.setting.models

data class AddMobileBody(
    val email:String,
    val g2f:String?,
    val mobile:String,
    val otp:String,
    val otp_enable:Boolean
)
