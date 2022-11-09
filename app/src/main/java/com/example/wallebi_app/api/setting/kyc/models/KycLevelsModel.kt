package com.example.wallebi_app.api.setting.kyc.models

data class KycLevelsModel(
    val name:Int,
    val crypto_max_daily_withdraw:Int,
    val fiat_max_daily_withdraw:Int
)