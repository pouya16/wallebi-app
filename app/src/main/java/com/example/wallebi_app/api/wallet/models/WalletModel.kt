package com.example.wallebi_app.api.wallet.models

class WalletModel(
    val id:Int,
    val name:String,
    val balance:BalanceModel
){
    override fun toString(): String = name
}
