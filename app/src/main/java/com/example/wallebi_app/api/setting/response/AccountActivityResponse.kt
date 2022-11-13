package com.example.wallebi_app.api.setting.response

import com.example.wallebi_app.api.ResponseParent
import com.example.wallebi_app.api.setting.models.AccountActivityModel

data class AccountActivityResponse(
    val msg:List<AccountActivityModel>, override var success: Boolean, override var err: String,
):ResponseParent()
