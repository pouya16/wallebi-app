package com.example.wallebi_app.api.bank

class IbanAccountsModel(
    val id:Int,
    val card_number:String,
    val iban:String,
    val bank_name:String
){
    override fun toString(): String {
        return iban
    }
}
