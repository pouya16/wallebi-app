package com.example.wallebi_app.api.setting.bodies

data class ChangePassBody(
    val confirm_password:String,
    val new_password:String,
    val old_password:String,
    val passphrase:String
)
