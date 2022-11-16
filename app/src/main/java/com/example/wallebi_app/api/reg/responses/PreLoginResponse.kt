package com.example.wallebi_app.api.reg.responses

import com.example.wallebi_app.api.reg.model.DataLogin
import com.google.gson.annotations.SerializedName

data class PreLoginResponse (

    val message:String,
    val availableIn:Int,
    val success: Boolean,
    val data: DataLogin,
    val err:String,
    val remain:Int

)