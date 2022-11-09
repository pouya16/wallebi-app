package com.example.wallebi_app.api.setting.response

import com.example.wallebi_app.api.setting.models.AccountActivityModel

data class AccountActivityResponse(
    val success:Boolean,
    val msg:List<AccountActivityModel>,
    val err:String
)
