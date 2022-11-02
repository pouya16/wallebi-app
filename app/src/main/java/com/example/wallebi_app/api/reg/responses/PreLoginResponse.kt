package com.example.wallebi_app.api.reg.responses

import com.example.wallebi_app.api.reg.model.DataLogin

data class PreLoginResponse (

    val success: String,
    val data: DataLogin,
    val err:String,
    val remain:Int

)