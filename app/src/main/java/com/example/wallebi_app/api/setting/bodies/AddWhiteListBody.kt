package com.example.wallebi_app.api.setting.bodies

data class AddWhiteListBody(
    val address:String,
    val coin:String?,
    val g2f:String?,
    val mark:String,
    val memo:String?,
    val network:String,
    val otp:String?
)
