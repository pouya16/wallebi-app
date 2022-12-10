package com.example.wallebi_app.api.data

class NetworkModel(
    val fullname:String,
    val network:String,
    val fee:Double,
    val ticker:String
){
    override fun toString(): String {
        return network
    }
}
