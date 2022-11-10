package com.example.wallebi_app.api.setting.response

import com.example.wallebi_app.api.setting.models.VipLevelDataModel

data class VipResponse(
    val success:Boolean,
    val data: VipLevelDataModel,
    val err:String
)
