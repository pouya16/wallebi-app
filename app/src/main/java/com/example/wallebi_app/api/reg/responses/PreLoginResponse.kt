package com.example.wallebi_app.api.reg.responses

data class PreLoginResponse(
    val success: Boolean?,
    val err:String?,
    val message:String?,
    val availableIn:Int?,
    val data: DataLogin?,
    val remain:Int?
)