package com.example.wallebi_app.api.setting.response

data class ValidateAddressModel(
    val valid:Boolean,
    val network:String,
    val message:String,
    val fullname:String
)