package com.example.wallebi_app.api.setting.models

data class ChangeOtpBody(
    val email:String,
    val g2f:String?,
    val otp:String,
    val type:String
)