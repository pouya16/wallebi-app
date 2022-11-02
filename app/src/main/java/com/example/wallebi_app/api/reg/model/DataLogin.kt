package com.example.wallebi_app.api.reg.model

data class DataLogin(
    val permission: PermissionModel,
    val type: String,
    val access_token: String,
    val refresh_token:String,
    val user:UserLoginModel
)
