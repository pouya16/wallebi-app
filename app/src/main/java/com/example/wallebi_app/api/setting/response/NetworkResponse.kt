package com.example.wallebi_app.api.setting.response

import com.example.wallebi_app.api.setting.models.NetworkDataModel

data class NetworkResponse(
    val success:Boolean,
    val msg:List<NetworkDataModel>,
    val err:String
)
