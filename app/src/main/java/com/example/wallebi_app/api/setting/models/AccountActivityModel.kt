package com.example.wallebi_app.api.setting.models

data class AccountActivityModel(
    val id:Int,
    val device:String,
    val action:String,
    val ip_address:String,
    val region:String,
    val status:String,
    val created_at:String
)
