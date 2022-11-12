package com.example.wallebi_app.api.setting.models

data class ValidateAddressDataModel(
    val fullname:String,
    val message:String,
    val network:String,
    val valid:Boolean
)