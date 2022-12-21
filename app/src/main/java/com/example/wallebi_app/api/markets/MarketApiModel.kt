package com.example.wallebi_app.api.markets

data class MarketApiModel(
    val id:Int,
    val ticker_from:String,
    val ticker_to:String,
    val coin_fullname:String,
    val coin_fullname_fa:String,
    val price:Double,
    val changed_amount_24:String,
    val low_24:String,
    val high_24:String,
    val volume_24:String,
    val value_24:String,
    val market_cap:String,
    val u:Int,
    val listing_date:String
)
