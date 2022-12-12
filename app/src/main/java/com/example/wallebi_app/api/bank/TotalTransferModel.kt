package com.example.wallebi_app.api.bank

data class TotalTransferModel(
    val total_monthly:Int,
    val total_daily:Int,
    val total_daily_iban:Int
)
