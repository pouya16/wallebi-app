package com.example.wallebi_app.api.setting.response

import com.example.wallebi_app.api.setting.models.CreateG2fData

data class CreateG2fResponse(
    val success:Boolean,
    val data:CreateG2fData,
    val err:String
)
