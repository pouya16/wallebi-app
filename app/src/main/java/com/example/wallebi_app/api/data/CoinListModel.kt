package com.example.wallebi_app.api.data

data class CoinListModel(
    val id:Int,
    val name:String,
    val fullname:String,
    val nickname: String,
    val is_active: Boolean
)