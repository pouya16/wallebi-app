package com.example.wallebi_app.api.reg.responses

import com.example.wallebi_app.api.reg.model.DataEmailVerify

data class VerifyEmailResponse(
    val success:Boolean,
    val data: DataEmailVerify,
    val err: String
)
