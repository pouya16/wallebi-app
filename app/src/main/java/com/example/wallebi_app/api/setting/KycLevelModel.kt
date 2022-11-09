package com.example.wallebi_app.api.setting

data class KycLevelModel(
    val level:Int,
    val max_withdraw:MaxWithdrawModel,
    val state:Int
)
