package com.example.wallebi_app.api.bank

data class FiatInfoModel(
    val total_transfer:TotalTransferModel,
    val minimum_amount:Double,
    val balance:Double
)
