package com.example.wallebi_app.api.data

data class AddressValidateResponse(
    val valid:Boolean,
    val network:String,
    val message:String,
    val fullname:String
)
