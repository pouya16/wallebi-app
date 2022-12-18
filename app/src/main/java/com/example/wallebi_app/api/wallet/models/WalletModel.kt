package com.example.wallebi_app.api.wallet.models

data class WalletModel(
    val id:Int,
    val name:String,
    val balance:BalanceModel
)
