package com.example.wallebi_app.api.wallet

data class MarketsModel(
    val id: Int,
    val ticker_from:String,
    val ticker_to:String,
    val line_price:Double
)