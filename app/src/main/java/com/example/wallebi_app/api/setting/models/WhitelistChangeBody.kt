package com.example.wallebi_app.api.setting.models

data class WhitelistChangeBody(
    val email:String,
    val g2f:String?,
    val is_withdraw_whitelist:Boolean,
    val otp:String?
)
