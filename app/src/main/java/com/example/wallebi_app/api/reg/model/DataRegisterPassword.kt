package com.example.wallebi_app.api.reg.model

import com.example.wallebi_app.api.reg.model.PermissionModel

data class DataRegisterPassword (
    val permissions: PermissionModel,
    val type: String,
    val access_token: String,
    val refresh_token: String
)