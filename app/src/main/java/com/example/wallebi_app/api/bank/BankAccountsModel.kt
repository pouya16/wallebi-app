package com.example.wallebi_app.api.bank

class BankAccountsModel(
    val id:Int,
    val card_number:String,
    val iban:String,
    val bank_name:String
){
    override fun toString(): String {
        return card_number
    }
}
