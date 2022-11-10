package com.example.wallebi_app.api.setting.models

import com.example.wallebi_app.api.reg.model.PermissionModel

data class CreateG2fData(
    val permissions:PermissionModel,
    val url:String,
    val secret:String
)
