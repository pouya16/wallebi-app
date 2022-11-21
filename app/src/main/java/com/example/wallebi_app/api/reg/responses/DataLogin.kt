package com.example.wallebi_app.api.reg.responses

import com.example.wallebi_app.api.reg.model.PermissionModel
import com.example.wallebi_app.api.reg.model.UserLoginModel

data class DataLogin(
    val permissions: PermissionModel,
    val type: String,
    val access_token: String,
    val refresh_token:String,
    val user: UserLoginModel
)
