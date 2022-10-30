package com.example.wallebi_app.api.reg.responses

import com.example.wallebi_app.api.reg.model.DataRegisterPassword

data class SetPasswordResponse(
    val success:Boolean,
    val data: DataRegisterPassword,

    )
