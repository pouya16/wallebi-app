package com.example.wallebi_app.api.setting.bodies

data class AddMobileBody(
    val email:String,
    val g2f:String?,
    val mobile:String,
    val otp:String,
    val otp_enable:Boolean
)
