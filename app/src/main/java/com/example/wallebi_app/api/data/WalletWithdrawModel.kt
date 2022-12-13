package com.example.wallebi_app.api.data

import java.nio.DoubleBuffer

data class WalletWithdrawModel(
    val balance:Double,
    val available_daily_withdraw:Double,
    val max_daily_withdraw:Double,
    val line_price:Double,
    val min_withdraw:Double,
    val max_withdraw:Double
)
