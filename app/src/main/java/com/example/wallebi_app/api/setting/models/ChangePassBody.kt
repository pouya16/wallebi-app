package com.example.wallebi_app.api.setting.models

data class ChangePassBody(
    val confirm_password:String,
    val new_password:String,
    val old_password:String,
    val passphrase:String
)
