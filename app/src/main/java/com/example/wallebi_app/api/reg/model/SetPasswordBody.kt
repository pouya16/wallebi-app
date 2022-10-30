package com.example.wallebi_app.api.reg.model

data class SetPasswordBody(
    val allow_key: String,
    val email: String,
    val new_password: String,
    val confirm_password: String
)