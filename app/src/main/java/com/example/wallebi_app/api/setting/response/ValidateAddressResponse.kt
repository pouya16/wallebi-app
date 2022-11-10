package com.example.wallebi_app.api.setting.response

data class ValidateAddressResponse(
    val success:Boolean,
    val msg:ValidateAddressModel,
    val err:String
)
