package com.example.wallebi_app.api.setting.models

data class VipLevelsModel(
    val level:Int,
    val maker_fee:Double,
    val taker_fee:Double,
    val min_amount:Int
)
