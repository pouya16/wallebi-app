package com.example.wallebi_app.api.reg.model

data class LoginBody(
    val password: String,
    val type: String,
    val username: String,
    val username_type: String
)
